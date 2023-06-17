package com.cerbon.better_totem_of_undying.utils;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class BTUUtils {

    public static void teleportOutOfVoid(LivingEntity livingEntity, Level level, int posX, int posY, int posZ){
        boolean isSlowFallingEnabled = BTUCommonConfigs.ENABLE_SLOW_FALLING.get();
        int slowFallingDuration = BTUCommonConfigs.SLOW_FALLING_DURATION.get();

        BlockPos positionNearby = randomTeleportNearby(livingEntity, level, posX, posY, posZ);

        if (positionNearby == null){
            livingEntity.teleportTo(posX, level.getMaxBuildHeight() + BTUCommonConfigs.TELEPORT_HEIGHT_OFFSET.get(), posZ);
            if (isSlowFallingEnabled){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, slowFallingDuration, 0));
            }
        }
    }

    public static BlockPos randomTeleportNearby(LivingEntity livingEntity, Level level, int posX, int posY, int posZ){
        BlockPos teleportPos = null;

        for (int i = 0; i < 16; i++) {
            double x = posX + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;
            double y = Mth.clamp(posY + (double) (livingEntity.getRandom().nextInt(16) - 8), level.getMinBuildHeight(), level.getMaxBuildHeight() - 1);
            double z = posZ + (livingEntity.getRandom().nextDouble() - 0.5D) * 16.0D;

            BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
            if (livingEntity.randomTeleport(x, y, z, true)) {
                teleportPos = pos;
                livingEntity.resetFallDistance();
                break;
            }
        }
        return teleportPos;
    }

    public static boolean isOutOfWorld(LivingEntity livingEntity, DamageSource damageSource){
        return damageSource.is(DamageTypes.OUT_OF_WORLD) && livingEntity.getY() < livingEntity.level.getMinBuildHeight();
    }

    //The method also checks the entity height to be sure it's not in the void. That way it doesn't conflict with the ability that saves the entity from dying in the void.
    public static boolean damageBypassInvulnerability(DamageSource damageSource, LivingEntity livingEntity){
        return damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !(livingEntity.getY() < livingEntity.level.getMinBuildHeight());
    }

    public static boolean isDimensionBlacklisted(Level level){
        return BTUCommonConfigs.BLACKLISTED_DIMENSIONS.get().contains(level.dimension().location().toString());
    }

    public static boolean isStructureBlacklisted(BlockPos pos, ServerLevel level){
        ArrayList<String> blackListedStructures = BTUCommonConfigs.BLACKLISTED_STRUCTURES.get();
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

    public static void destroyBlocksWhenSuffocatingOrFullyFrozen(LivingEntity livingEntity, Level level){
        boolean isDestroyBlocksWhenSuffocatingEnabled = BTUCommonConfigs.DESTROY_BLOCKS_WHEN_SUFFOCATING.get();
        boolean isDestroyPowderSnowWhenFullyFrozenEnabled = BTUCommonConfigs.DESTROY_POWDER_SNOW_WHEN_FULLY_FROZEN.get();

        if ((livingEntity.isInWall() && isDestroyBlocksWhenSuffocatingEnabled) || (livingEntity.isFullyFrozen() && isDestroyPowderSnowWhenFullyFrozenEnabled)) {
            BlockPos entityPos = livingEntity.blockPosition();
            BlockState blockAtEntityPos = level.getBlockState(entityPos);
            BlockState blockAboveEntityPos = level.getBlockState(entityPos.above());

            if (blockAtEntityPos.getBlock() != Blocks.BEDROCK && blockAboveEntityPos.getBlock() != Blocks.BEDROCK) {
                int i = 2;
                while (true){
                    if (level.getBlockState(entityPos.above(i)).getBlock() instanceof FallingBlock){
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

    public static void knockBackMobsAway(LivingEntity livingEntity, Level level){
        boolean isKnockBackMobsAwayEnabled = BTUCommonConfigs.KNOCK_BACK_MOBS_AWAY.get();

        if (isKnockBackMobsAwayEnabled){
            double radius = BTUCommonConfigs.KNOCK_BACK_RADIUS.get();
            double strength = BTUCommonConfigs.KNOCK_BACK_STRENGTH.get();
            AABB aabb = livingEntity.getBoundingBox().inflate(radius);
            List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, aabb);

            for (LivingEntity entity : nearbyEntities){
                if (!entity.is(livingEntity)){
                    entity.knockback(strength, livingEntity.getX() - entity.getX(), livingEntity.getZ() - entity.getZ());
                }
            }
        }
    }
}
