package com.cerbon.better_totem_of_undying.utils;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import com.cerbon.cerbons_api.api.static_utilities.CapabilityUtils;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
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
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BTUUtils {

    public static boolean canSaveFromDeath(@NotNull LivingEntity livingEntity, DamageSource damageSource) {
        ItemStack itemStack = getTotemItemStack(livingEntity);
        BlockPos entityPos = livingEntity.blockPosition();
        Level level = livingEntity.level();

        boolean isTotemOnCooldown = livingEntity instanceof ServerPlayer player && player.getCooldowns().isOnCooldown(Items.TOTEM_OF_UNDYING);
        boolean isTeleportOutOfVoidEnabled = BTUCommonConfigs.TELEPORT_OUT_OF_VOID.get();

        if (itemStack == null || isDimensionBlacklisted(level) || isStructureBlacklisted(entityPos, (ServerLevel) level) || damageBypassInvulnerability(damageSource, livingEntity) || (!isTeleportOutOfVoidEnabled && isInVoid(livingEntity, damageSource)) || isTotemOnCooldown)
            return false;

        if(livingEntity instanceof ServerPlayer player) {
            giveUseStatAndCriterion(itemStack, player);
            addCooldown(itemStack, player, BTUCommonConfigs.COOLDOWN.get());
        }

        itemStack.shrink(1);

        if (BTUCommonConfigs.REMOVE_ALL_EFFECTS.get()) livingEntity.removeAllEffects();
        livingEntity.setHealth(BTUCommonConfigs.SET_HEALTH.get());

        applyTotemEffects(livingEntity, damageSource);
        increaseFoodLevel(livingEntity, BTUCommonConfigs.SET_FOOD_LEVEL.get());
        destroyBlocksWhenSuffocatingOrFullyFrozen(livingEntity, level);
        knockbackMobsAway(livingEntity, level);
        teleportOutOfVoid(livingEntity, level, damageSource);

        level.broadcastEntityEvent(livingEntity, (byte) 35);

        return true;
    }

    public static boolean isDimensionBlacklisted(@NotNull Level level) {
        return BTUCommonConfigs.BLACKLISTED_DIMENSIONS.get().contains(level.dimension().location().toString());
    }

    public static boolean isStructureBlacklisted(BlockPos pos, @NotNull ServerLevel level) {
        List<? extends String> blackListedStructures = BTUCommonConfigs.BLACKLISTED_STRUCTURES.get();

        boolean flag = false;
        for (String structureKey : blackListedStructures){
            Structure structure = RegistryUtils.getStructureByKey(structureKey, level);

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
        if (MiscUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID) && BTUCommonConfigs.USE_TOTEM_FROM_CHARM_SLOT.get())
            return CuriosApi.getCuriosHelper().findFirstCurio(livingEntity, Items.TOTEM_OF_UNDYING).map(SlotResult::stack).orElse(null);

        return null;
    }

    public static @Nullable ItemStack getTotemFromInventory(LivingEntity livingEntity) {
        if (BTUCommonConfigs.USE_TOTEM_FROM_INVENTORY.get() && livingEntity instanceof ServerPlayer player)
            for (ItemStack itemStack : player.getInventory().items)
                if (itemStack.is(Items.TOTEM_OF_UNDYING)) return itemStack;

        return null;
    }

    public static @Nullable ItemStack getTotemFromHands(LivingEntity livingEntity){
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

    public static void addCooldown(ItemStack itemStack, ServerPlayer player, int cooldown){
        if (BTUCommonConfigs.ADD_COOLDOWN.get())
            player.getCooldowns().addCooldown(itemStack.getItem(), cooldown);
    }

    public static void applyTotemEffects(LivingEntity livingEntity, DamageSource damageSource){
        int fireResistanceEffectDuration = BTUCommonConfigs.FIRE_RESISTANCE_DURATION.get();
        int regenerationEffectDuration = BTUCommonConfigs.REGENERATION_DURATION.get();
        int regenerationEffectAmplifier = BTUCommonConfigs.REGENERATION_AMPLIFIER.get();
        int absorptionEffectDuration = BTUCommonConfigs.ABSORPTION_DURATION.get();
        int absorptionEffectAmplifier = BTUCommonConfigs.ABSORPTION_AMPLIFIER.get();
        int waterBreathingEffectDuration = BTUCommonConfigs.WATER_BREATHING_DURATION.get();

        if (BTUCommonConfigs.APPLY_EFFECTS_ONLY_WHEN_NEEDED.get()) {
            if (livingEntity.isOnFire() && BTUCommonConfigs.ENABLE_FIRE_RESISTANCE.get()) livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            if (livingEntity.isInWaterOrBubble() && BTUCommonConfigs.ENABLE_WATER_BREATHING.get()) livingEntity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
        } else {
            if (BTUCommonConfigs.ENABLE_FIRE_RESISTANCE.get()) livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            if (BTUCommonConfigs.ENABLE_WATER_BREATHING.get()) livingEntity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
        }
        if (BTUCommonConfigs.ENABLE_REGENERATION.get()) livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regenerationEffectDuration, regenerationEffectAmplifier));
        if (BTUCommonConfigs.ENABLE_ABSORPTION.get()) livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, absorptionEffectDuration, absorptionEffectAmplifier));

        BTUCommonConfigs.CUSTOM_EFFECTS.get().forEach(customEffectProperties -> {
            if (!customEffectProperties.isEmpty()) {
                try {
                    String damageTypeKey = (String) customEffectProperties.get(0);
                    String mobEffectKey = (String) customEffectProperties.get(1);
                    int effectDuration = (int) customEffectProperties.get(2);
                    int effectAmplifier = (int) customEffectProperties.get(3);

                    ResourceKey<DamageType> damageType = getDamageTypeByKey(damageTypeKey, (ServerLevel) livingEntity.level());
                    MobEffect mobEffect = RegistryUtils.getMobEffectByKey(mobEffectKey);

                    if (damageType != null && damageSource.is(damageType) || damageTypeKey.equals("any"))
                        livingEntity.addEffect(new MobEffectInstance(mobEffect, effectDuration, effectAmplifier));

                } catch (Exception e) {
                    BetterTotemOfUndying.LOGGER.error("Better Totem of Undying error: Couldn't apply custom effect. Wrong/Missing parameter: {}", customEffectProperties, e);
                }
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

    public static void increaseFoodLevel(LivingEntity livingEntity, int foodLevel) {
        if (BTUCommonConfigs.ENABLE_INCREASE_FOOD_LEVEL.get() && livingEntity instanceof ServerPlayer player) {
            int currentFoodLevel = player.getFoodData().getFoodLevel();
            int minimumFoodLevel = BTUCommonConfigs.MINIMUM_FOOD_LEVEL.get();

            if (currentFoodLevel <= minimumFoodLevel)
                player.getFoodData().setFoodLevel(foodLevel);
        }
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
        return (livingEntity.isInWall() && BTUCommonConfigs.DESTROY_BLOCKS_WHEN_SUFFOCATING.get()) ||
                (livingEntity.isFullyFrozen() && BTUCommonConfigs.DESTROY_POWDER_SNOW_WHEN_FULLY_FROZEN.get());
    }

    public static boolean canDestroy(@NotNull BlockState block) {
        return !block.is(BTUConstants.TOTEM_CANT_DESTROY_TAG) && !block.is(Blocks.BEDROCK) && !block.is(Blocks.END_PORTAL_FRAME);
    }

    public static boolean isInstanceOfFallingBlock(@NotNull BlockPos pos, @NotNull Level level, int distance) {
        return level.getBlockState(pos.above(distance)).getBlock() instanceof FallingBlock;
    }

    public static void knockbackMobsAway(LivingEntity livingEntity, Level level) {
        if (BTUCommonConfigs.KNOCKBACK_MOBS_AWAY.get()) {
            List<LivingEntity> nearbyEntities = getNearbyEntities(livingEntity, level, BTUCommonConfigs.KNOCKBACK_RADIUS.get());
            double strength = BTUCommonConfigs.KNOCKBACK_STRENGTH.get();

            for (LivingEntity entity : nearbyEntities)
                entity.knockback(strength, livingEntity.getX() - entity.getX(), livingEntity.getZ() - entity.getZ());
        }
    }

    public static @NotNull List<LivingEntity> getNearbyEntities(@NotNull LivingEntity livingEntity, @NotNull Level level, double radius) {
        return level.getEntitiesOfClass(LivingEntity.class, livingEntity.getBoundingBox().inflate(radius), livingEntity1 -> livingEntity1 != livingEntity);
    }

    public static void teleportOutOfVoid(LivingEntity livingEntity, Level level, DamageSource damageSource) {
        if (isInVoid(livingEntity, damageSource)) {
            List<BlockPos> lastBlocksPos = CapabilityUtils.getLastBlockPositions(livingEntity);
            BlockPos lastBlockPos = CapabilityUtils.getLastBlockPos(livingEntity);

            Optional<BlockPos> positionNearby = lastBlocksPos.stream()
                    .map(blockPos -> randomTeleportNearby(livingEntity, level, blockPos))
                    .filter(Objects::nonNull).findFirst();

            if (positionNearby.isEmpty()) {
                livingEntity.teleportTo(lastBlockPos.getX(), level.getMaxBuildHeight() + BTUCommonConfigs.TELEPORT_HEIGHT_OFFSET.get(), lastBlockPos.getZ());
                applySlowFallingEffect(livingEntity);
            }
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
                livingEntity.resetFallDistance();
                break;
            }
        }
        return teleportPos;
    }

    public static void applySlowFallingEffect(LivingEntity livingEntity) {
        int duration = BTUCommonConfigs.SLOW_FALLING_DURATION.get();

        if (BTUCommonConfigs.ENABLE_SLOW_FALLING.get())
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, duration, 0));
    }
}
