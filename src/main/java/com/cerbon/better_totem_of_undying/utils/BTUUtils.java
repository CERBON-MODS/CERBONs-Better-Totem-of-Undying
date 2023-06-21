package com.cerbon.better_totem_of_undying.utils;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BTUUtils {

    public static boolean isModLoaded(String modId){
        return ModList.get().isLoaded(modId);
    }

    public static boolean canSaveFromDeath(LivingEntity livingEntity, DamageSource damageSource){
        boolean isTotemOnCooldown = livingEntity instanceof ServerPlayer player && player.getCooldowns().isOnCooldown(Items.TOTEM_OF_UNDYING);
        boolean isTeleportOutOfVoidEnabled = BTUCommonConfigs.TELEPORT_OUT_OF_VOID.get();
        BlockPos entityPos = livingEntity.blockPosition();
        Level level = livingEntity.level;

        if (isDimensionBlacklisted(level) || isStructureBlacklisted(entityPos, (ServerLevel) level) || damageBypassInvulnerability(damageSource, livingEntity) || (!isTeleportOutOfVoidEnabled && isInVoid(livingEntity, damageSource)) || isTotemOnCooldown){
            return false;
        }else {
            ItemStack itemStack = getTotemItemStack(livingEntity);

            if(itemStack != null){
                if(livingEntity instanceof ServerPlayer player){
                    giveUseStatAndCriterion(itemStack, player);
                    addCooldown(itemStack, player, BTUCommonConfigs.COOLDOWN.get());
                }

                itemStack.shrink(1);

                if (BTUCommonConfigs.REMOVE_ALL_EFFECTS.get())
                    livingEntity.removeAllEffects();

                livingEntity.setHealth(BTUCommonConfigs.SET_HEALTH.get());

                applyTotemEffects(livingEntity);
                increaseFoodLevel(livingEntity);
                destroyBlocksWhenSuffocatingOrFullyFrozen(livingEntity, level);
                knockbackMobsAway(livingEntity, level);
                teleportOutOfVoid(livingEntity, level, damageSource);

                level.broadcastEntityEvent(livingEntity, (byte) 35);
            }
            return itemStack != null;
        }
    }

    public static boolean isDimensionBlacklisted(@NotNull Level level){
        return BTUCommonConfigs.BLACKLISTED_DIMENSIONS.get().contains(level.dimension().location().toString());
    }

    public static boolean isStructureBlacklisted(BlockPos pos, @NotNull ServerLevel level){
        List<? extends String> blackListedStructures = BTUCommonConfigs.BLACKLISTED_STRUCTURES.get();
        Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        boolean flag = false;
        for (String structureName : blackListedStructures){
            Structure structure = structureRegistry.get(new ResourceLocation(structureName));

            if (structure != null){
                if (level.structureManager().getStructureAt(pos, structure).isValid()){
                    flag = true;
                }
            }
        }
        return flag;
    }

    public static boolean damageBypassInvulnerability(@NotNull DamageSource damageSource, LivingEntity livingEntity){
        return damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !(livingEntity.getY() < livingEntity.level.getMinBuildHeight());
    }

    public static boolean isInVoid(LivingEntity livingEntity, @NotNull DamageSource damageSource){
        return damageSource.is(DamageTypes.OUT_OF_WORLD) && livingEntity.getY() < livingEntity.level.getMinBuildHeight();
    }

    public static ItemStack getTotemItemStack(LivingEntity livingEntity){
        List<ItemStack> possibleTotemStacks = filterPossibleTotemStacks(getTotemFromCharmSlot(livingEntity), getTotemFromInventory(livingEntity), getTotemFromHands(livingEntity));
        return possibleTotemStacks.stream().findFirst().orElse(null);
    }

    public static List<ItemStack> filterPossibleTotemStacks(ItemStack... stacks){
        return Arrays.stream(stacks).filter(Objects::nonNull).toList();
    }

    public static ItemStack getTotemFromCharmSlot(LivingEntity livingEntity){
        if (isModLoaded(BTUConstants.CURIOS_MOD_ID) && BTUCommonConfigs.USE_TOTEM_FROM_CHARM_SLOT.get()){
            return CuriosApi.getCuriosHelper().findFirstCurio(livingEntity, Items.TOTEM_OF_UNDYING).map(SlotResult::stack).orElse(null);
        }
        return null;
    }

    public static ItemStack getTotemFromInventory(LivingEntity livingEntity){
        if (BTUCommonConfigs.USE_TOTEM_FROM_INVENTORY.get() && livingEntity instanceof ServerPlayer player){
            for (ItemStack itemStack : player.getInventory().items){
                if (itemStack.is(Items.TOTEM_OF_UNDYING)) return itemStack;
            }
        }
        return null;
    }

    public static ItemStack getTotemFromHands(LivingEntity livingEntity){
        for (InteractionHand interactionHand : InteractionHand.values()){
            ItemStack itemStack = livingEntity.getItemInHand(interactionHand);
            if (itemStack.is(Items.TOTEM_OF_UNDYING)) return itemStack;
        }
        return null;
    }

    public static void giveUseStatAndCriterion(ItemStack itemStack, ServerPlayer player){
        if (!itemStack.isEmpty()){
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            CriteriaTriggers.USED_TOTEM.trigger(player, itemStack);
        }
    }

    public static void addCooldown(ItemStack itemStack, ServerPlayer player, int cooldown){
        if (BTUCommonConfigs.ADD_COOLDOWN.get()){
            player.getCooldowns().addCooldown(itemStack.getItem(), cooldown);
        }
    }

    public static void applyTotemEffects(LivingEntity livingEntity){
        boolean isApplyEffectsOnlyWhenNeededEnabled = BTUCommonConfigs.APPLY_EFFECTS_ONLY_WHEN_NEEDED.get();

        boolean isFireResistanceEffectEnabled = BTUCommonConfigs.ENABLE_FIRE_RESISTANCE.get();
        int fireResistanceEffectDuration = BTUCommonConfigs.FIRE_RESISTANCE_DURATION.get();

        boolean isRegenerationEffectEnabled = BTUCommonConfigs.ENABLE_REGENERATION.get();
        int regenerationEffectDuration = BTUCommonConfigs.REGENERATION_DURATION.get();
        int regenerationEffectAmplifier = BTUCommonConfigs.REGENERATION_AMPLIFIER.get();

        boolean isAbsorptionEffectEnabled = BTUCommonConfigs.ENABLE_ABSORPTION.get();
        int absorptionEffectDuration = BTUCommonConfigs.ABSORPTION_DURATION.get();
        int absorptionEffectAmplifier = BTUCommonConfigs.ABSORPTION_AMPLIFIER.get();

        boolean isWaterBreathingEffectEnabled = BTUCommonConfigs.ENABLE_WATER_BREATHING.get();
        int waterBreathingEffectDuration = BTUCommonConfigs.WATER_BREATHING_DURATION.get();

        if (isApplyEffectsOnlyWhenNeededEnabled) {
            if (livingEntity.isOnFire() && isFireResistanceEffectEnabled) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            }
            if (livingEntity.isInWaterOrBubble() && isWaterBreathingEffectEnabled) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
            }
        } else {
            if (isFireResistanceEffectEnabled) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            }
            if (isWaterBreathingEffectEnabled) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
            }
        }

        if (isRegenerationEffectEnabled) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regenerationEffectDuration, regenerationEffectAmplifier));
        }
        if (isAbsorptionEffectEnabled) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, absorptionEffectDuration, absorptionEffectAmplifier));
        }
    }

    public static void increaseFoodLevel(LivingEntity livingEntity){
        boolean isIncreaseFoodLevelEnabled = BTUCommonConfigs.ENABLE_INCREASE_FOOD_LEVEL.get();

        if (livingEntity instanceof ServerPlayer serverPlayer && isIncreaseFoodLevelEnabled) {
            int currentFoodLevel = serverPlayer.getFoodData().getFoodLevel();
            int minimumFoodLevel = BTUCommonConfigs.MINIMUM_FOOD_LEVEL.get();
            int foodLevel = BTUCommonConfigs.SET_FOOD_LEVEL.get();

            if (currentFoodLevel <= minimumFoodLevel) {
                serverPlayer.getFoodData().setFoodLevel(foodLevel);
            }
        }
    }

    public static void destroyBlocksWhenSuffocatingOrFullyFrozen(@NotNull LivingEntity livingEntity, Level level){
        boolean isDestroyBlocksWhenSuffocatingEnabled = BTUCommonConfigs.DESTROY_BLOCKS_WHEN_SUFFOCATING.get();
        boolean isDestroyPowderSnowWhenFullyFrozenEnabled = BTUCommonConfigs.DESTROY_POWDER_SNOW_WHEN_FULLY_FROZEN.get();

        if ((livingEntity.isInWall() && isDestroyBlocksWhenSuffocatingEnabled) || (livingEntity.isFullyFrozen() && isDestroyPowderSnowWhenFullyFrozenEnabled)) {
            BlockPos entityPos = livingEntity.blockPosition();
            BlockState blockAtEntityPos = level.getBlockState(entityPos);
            BlockState blockAboveEntityPos = level.getBlockState(entityPos.above());

            if (!blockAtEntityPos.is(BTUConstants.TOTEM_CANT_DESTROY_TAG) && !blockAboveEntityPos.is(BTUConstants.TOTEM_CANT_DESTROY_TAG)) {
                int i = 2;
                while (true){
                    if (level.getBlockState(entityPos.above(i)).getBlock() instanceof FallingBlock && !level.getBlockState(entityPos.above(i)).is(BTUConstants.TOTEM_CANT_DESTROY_TAG)){
                        level.destroyBlock(entityPos.above(i), true);
                        i++;
                    }else{
                        break;
                    }
                }
                level.destroyBlock(entityPos, true);
                level.destroyBlock(entityPos.above(), true);
            }
        }
    }

    public static void knockbackMobsAway(LivingEntity livingEntity, Level level){
        boolean isKnockbackMobsAwayEnabled = BTUCommonConfigs.KNOCKBACK_MOBS_AWAY.get();

        if (isKnockbackMobsAwayEnabled){
            double radius = BTUCommonConfigs.KNOCKBACK_RADIUS.get();
            double strength = BTUCommonConfigs.KNOCKBACK_STRENGTH.get();
            AABB aabb = livingEntity.getBoundingBox().inflate(radius);
            List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, aabb);

            for (LivingEntity entity : nearbyEntities){
                if (!entity.is(livingEntity)){
                    entity.knockback(strength, livingEntity.getX() - entity.getX(), livingEntity.getZ() - entity.getZ());
                }
            }
        }
    }

    public static void teleportOutOfVoid(LivingEntity livingEntity, Level level, DamageSource damageSource){
        if (isInVoid(livingEntity, damageSource)){
            BlockPos lastBlockPos = BlockPos.of(((ILivingEntityMixin) livingEntity).getLastBlockPos());

            BlockPos positionNearby = randomTeleportNearby(livingEntity, level, lastBlockPos);
            if (positionNearby == null){
                livingEntity.teleportTo(lastBlockPos.getX(), level.getMaxBuildHeight() + BTUCommonConfigs.TELEPORT_HEIGHT_OFFSET.get(), lastBlockPos.getZ());
                applySlowFallingEffect(livingEntity);
            }
        }
    }

    public static BlockPos randomTeleportNearby(LivingEntity livingEntity, Level level, BlockPos blockPos){
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

    public static void applySlowFallingEffect(LivingEntity livingEntity){
        boolean isSlowFallingEffectEnabled = BTUCommonConfigs.ENABLE_SLOW_FALLING.get();
        int slowFallingEffectDuration = BTUCommonConfigs.SLOW_FALLING_DURATION.get();

        if (isSlowFallingEffectEnabled){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, slowFallingEffectDuration, 0));
        }
    }
}
