package com.cerbon.better_totem_of_undying.util;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.config.BTUConfigs;
import com.cerbon.better_totem_of_undying.config.custom.DefaultTotemFeatures;
import com.cerbon.better_totem_of_undying.config.custom.NewTotemFeatures;
import com.cerbon.better_totem_of_undying.platform.BTUServices;
import com.cerbon.better_totem_of_undying.util.mixin.ILivingEntityMixin;
import com.cerbon.cerbons_api.api.static_utilities.RegistryUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BTUUtils {
    private static final BTUConfigs btuConfigs = BetterTotemOfUndying.config;

    public static boolean canSaveFromDeath(@NotNull LivingEntity livingEntity, DamageSource damageSource) {
        ItemStack itemStack = getTotemItemStack(livingEntity);
        BlockPos entityPos = livingEntity.blockPosition();
        Level level = livingEntity.level();

        boolean isTotemOnCooldown = livingEntity instanceof ServerPlayer player && player.getCooldowns().isOnCooldown(Items.TOTEM_OF_UNDYING);
        boolean isTeleportOutOfVoidEnabled = btuConfigs.newTotemFeatures.teleportOutOfVoid.enabled;

        if (itemStack == null || isDimensionBlacklisted(level) || isStructureBlacklisted(entityPos, (ServerLevel) level) || damageBypassInvulnerability(damageSource, livingEntity) || (!isTeleportOutOfVoidEnabled && isInVoid(livingEntity, damageSource)) || isTotemOnCooldown)
            return false;

        if(livingEntity instanceof ServerPlayer player) {
            giveUseStatAndCriterion(itemStack, player);
            addCooldown(itemStack, player, btuConfigs.newTotemFeatures.cooldown.cooldown);
        }

        itemStack.shrink(1);

        if (btuConfigs.defaultTotemFeatures.clearEffectsUponUse) livingEntity.removeAllEffects();
        livingEntity.setHealth(btuConfigs.defaultTotemFeatures.healthUponUse);

        applyTotemEffects(livingEntity, damageSource);
        destroyBlocksWhenSuffocatingOrFullyFrozen(livingEntity, level);
        knockbackMobsAway(livingEntity, level);
        teleportOutOfVoid(livingEntity, level, damageSource);

        level.broadcastEntityEvent(livingEntity, (byte) 35);

        return true;
    }

    public static boolean isDimensionBlacklisted(@NotNull Level level) {
        for (var blackListedDimension : btuConfigs.blacklists.blacklistedDimensions)
            if (level.dimension().location().toString().equals(blackListedDimension.name))
                return true;

        return false;
    }

    public static boolean isStructureBlacklisted(BlockPos pos, @NotNull ServerLevel level) {
        var blackListedStructures = btuConfigs.blacklists.blacklistedStructures;

        boolean flag = false;
        for (var blackListedStructure : blackListedStructures){
            Structure structure = RegistryUtils.getStructureByKey(blackListedStructure.name, level);

            if (structure != null)
                if (level.structureManager().getStructureAt(pos, structure).isValid())
                    flag = true;
        }
        return flag;
    }

    public static boolean damageBypassInvulnerability(@NotNull DamageSource damageSource, LivingEntity livingEntity) {
        return damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !(livingEntity.getY() < livingEntity.level().getMinBuildHeight());
    }

    public static boolean isInVoid(LivingEntity livingEntity, @NotNull DamageSource damageSource) {
        return damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) && livingEntity.getY() < livingEntity.level().getMinBuildHeight();
    }

    public static ItemStack getTotemItemStack(LivingEntity livingEntity) {
        List<ItemStack> possibleTotemStacks = filterPossibleTotemStacks(getTotemFromCharmSlot(livingEntity), getTotemFromInventory(livingEntity), getTotemFromHands(livingEntity));
        return possibleTotemStacks.stream().findFirst().orElse(null);
    }

    public static List<ItemStack> filterPossibleTotemStacks(ItemStack... stacks) {
        return Arrays.stream(stacks).filter(Objects::nonNull).toList();
    }


    public static @Nullable ItemStack getTotemFromCharmSlot(LivingEntity livingEntity) {
        return BTUServices.PLATFORM_CHARM_HELPER.getTotemFromCharmSlot(livingEntity);
    }

    public static @Nullable ItemStack getTotemFromInventory(LivingEntity livingEntity) {
        if (btuConfigs.newTotemFeatures.canUseTotemFromInventory && livingEntity instanceof ServerPlayer player)
            for (ItemStack itemStack : player.getInventory().items)
                if (itemStack.is(Items.TOTEM_OF_UNDYING)) return itemStack;

        return null;
    }

    public static @Nullable ItemStack getTotemFromHands(LivingEntity livingEntity) {
        for (InteractionHand interactionHand : InteractionHand.values()) {
            ItemStack itemStack = livingEntity.getItemInHand(interactionHand);
            if (itemStack.is(Items.TOTEM_OF_UNDYING)) return itemStack;
        }
        return null;
    }

    public static void giveUseStatAndCriterion(@NotNull ItemStack itemStack, ServerPlayer player) {
        if (!itemStack.isEmpty()) {
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            CriteriaTriggers.USED_TOTEM.trigger(player, itemStack);
        }
    }

    public static void addCooldown(ItemStack itemStack, ServerPlayer player, int cooldown) {
        if (btuConfigs.newTotemFeatures.cooldown.enabled)
            player.getCooldowns().addCooldown(itemStack.getItem(), cooldown);
    }

    public static void applyTotemEffects(LivingEntity livingEntity, DamageSource damageSource) {
        DefaultTotemFeatures defaultTotemFeatures = btuConfigs.defaultTotemFeatures;
        NewTotemFeatures newTotemFeatures = btuConfigs.newTotemFeatures;

        int fireResistanceEffectDuration = defaultTotemFeatures.fireResistance.duration;
        int regenerationEffectDuration = defaultTotemFeatures.regeneration.duration;
        int regenerationEffectAmplifier = defaultTotemFeatures.regeneration.amplifier;
        int absorptionEffectDuration = defaultTotemFeatures.absorption.duration;
        int absorptionEffectAmplifier = defaultTotemFeatures.absorption.amplifier;
        int waterBreathingEffectDuration = newTotemFeatures.waterBreathing.duration;

        if (newTotemFeatures.applyEffectsOnlyWhenNecessary) {
            if (livingEntity.isOnFire() && defaultTotemFeatures.fireResistance.enabled) livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            if (livingEntity.isInWaterOrBubble() && newTotemFeatures.waterBreathing.enabled) livingEntity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
        } else {
            if (defaultTotemFeatures.fireResistance.enabled) livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            if (newTotemFeatures.waterBreathing.enabled) livingEntity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
        }
        if (defaultTotemFeatures.regeneration.enabled) livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regenerationEffectDuration, regenerationEffectAmplifier));
        if (defaultTotemFeatures.absorption.enabled) livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, absorptionEffectDuration, absorptionEffectAmplifier));

        newTotemFeatures.customEffects.forEach(customEffect -> {
            try {
                ResourceKey<DamageType> damageType = getDamageTypeByKey(customEffect.damageType, (ServerLevel) livingEntity.level());
                MobEffect mobEffect = RegistryUtils.getMobEffectByKey(customEffect.effect);

                if ("any".equals(customEffect.damageType) || (damageType != null && damageSource.is(damageType)))
                    livingEntity.addEffect(new MobEffectInstance(mobEffect, customEffect.duration, customEffect.amplifier));

            } catch (Exception e) {
                BTUConstants.LOGGER.error("Better Totem of Undying error: Couldn't apply custom effect: {}", customEffect, e);
            }
        });
    }

    public static ResourceKey<DamageType> getDamageTypeByKey(String key, ServerLevel level){
        if (!key.equals("any")) {
            Registry<DamageType> damageTypeRegistry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
            return damageTypeRegistry.getResourceKey(Objects.requireNonNull(damageTypeRegistry.get(new ResourceLocation(key)))).orElse(null);
        }
        return null;
    }

    public static void destroyBlocksWhenSuffocatingOrFullyFrozen(@NotNull LivingEntity livingEntity, Level level) {
        if (isInWallOrFullyFrozen(livingEntity)) {
            BlockPos blockPos = livingEntity.blockPosition();
            BlockState blockAtEntityPos = level.getBlockState(blockPos);
            BlockState blockAboveEntityPos = level.getBlockState(blockPos.above());

            if (canDestroy(blockAtEntityPos)) level.destroyBlock(blockPos, true);
            if (canDestroy(blockAboveEntityPos)) level.destroyBlock(blockPos.above(), true);

            int distance = 2;
            while (true) {
                if (isInstanceOfFallingBlock(blockPos, level, distance) && canDestroy(level.getBlockState(blockPos.above(distance)))) {
                    level.destroyBlock(blockPos.above(distance), true);
                    distance++;
                } else break;
            }
        }
    }

    public static boolean isInWallOrFullyFrozen(@NotNull LivingEntity livingEntity) {
        return (livingEntity.isInWall() && btuConfigs.newTotemFeatures.destroyBlocks.whenSuffocating) ||
                (livingEntity.isFullyFrozen() && btuConfigs.newTotemFeatures.destroyBlocks.whenFullyFrozen);
    }

    public static boolean canDestroy(@NotNull BlockState block) {
        return !block.is(BTUConstants.TOTEM_CANT_DESTROY_TAG) && !block.is(Blocks.BEDROCK) && !block.is(Blocks.END_PORTAL_FRAME);
    }

    public static boolean isInstanceOfFallingBlock(@NotNull BlockPos pos, @NotNull Level level, int distance) {
        return level.getBlockState(pos.above(distance)).getBlock() instanceof FallingBlock;
    }

    public static void knockbackMobsAway(LivingEntity livingEntity, Level level) {
        if (btuConfigs.newTotemFeatures.knockbackMobs.enabled) {
            List<LivingEntity> nearbyEntities = getNearbyEntities(livingEntity, level, btuConfigs.newTotemFeatures.knockbackMobs.radius);
            double strength = btuConfigs.newTotemFeatures.knockbackMobs.strength;

            for (LivingEntity entity : nearbyEntities)
                entity.knockback(strength, livingEntity.getX() - entity.getX(), livingEntity.getZ() - entity.getZ());
        }
    }

    public static @NotNull List<LivingEntity> getNearbyEntities(@NotNull LivingEntity livingEntity, @NotNull Level level, double radius) {
        return level.getEntitiesOfClass(LivingEntity.class, livingEntity.getBoundingBox().inflate(radius), livingEntity1 -> livingEntity1 != livingEntity);
    }

    public static void teleportOutOfVoid(LivingEntity livingEntity, Level level, DamageSource damageSource) {
        if (isInVoid(livingEntity, damageSource)) {
            BlockPos lastBlockPos = ((ILivingEntityMixin) livingEntity).btu_getLastBlockPos();

            BlockPos positionNearby = randomTeleportNearby(livingEntity, level, lastBlockPos);
            if (positionNearby == null)
                livingEntity.teleportTo(lastBlockPos.getX(), level.getMaxBuildHeight() + btuConfigs.newTotemFeatures.teleportOutOfVoid.teleportHeightOffset, lastBlockPos.getZ());

            setFallDamageImmune(livingEntity);
        }
    }

    public static BlockPos randomTeleportNearby(LivingEntity livingEntity, Level level, BlockPos blockPos) {
        BlockPos teleportPos = null;

        for (int i = 0; i < 16; i++) {
            double x = blockPos.getX() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
            double y = Mth.clamp(blockPos.getY() + (double) (livingEntity.getRandom().nextInt(16) - 8), level.getMinBuildHeight(), level.getMaxBuildHeight() - 1);
            double z = blockPos.getZ() + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;

            BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
            if (livingEntity.randomTeleport(x, y, z, true)) {
                teleportPos = pos;
                break;
            }
        }
        return teleportPos;
    }

    public static void setFallDamageImmune(LivingEntity livingEntity) {
        ((ILivingEntityMixin) livingEntity).btu_setFallDamageImmune(true);
    }

    public static void resetFallDamageImmune(LivingEntity livingEntity) {
        if (((ILivingEntityMixin) livingEntity).btu_isFallDamageImmune()) {
            boolean canPlayerFly = false;
            boolean isInWater = livingEntity.isInWater();
            boolean isInCobweb = livingEntity.level().getBlockState(livingEntity.blockPosition()).getBlock().equals(Blocks.COBWEB);

            if (livingEntity instanceof ServerPlayer player && (player.getAbilities().flying || player.getAbilities().mayfly))
                canPlayerFly = true;

            if (canPlayerFly || isInWater || isInCobweb)
                ((ILivingEntityMixin) livingEntity).btu_setFallDamageImmune(false);
        }
    }
}
