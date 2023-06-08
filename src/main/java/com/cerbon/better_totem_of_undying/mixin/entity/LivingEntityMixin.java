package com.cerbon.better_totem_of_undying.mixin.entity;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, net.minecraftforge.common.extensions.IForgeLivingEntity  {

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow public abstract boolean addEffect(MobEffectInstance pEffectInstance);

    @Shadow public abstract void setHealth(float pHealth);

    @Shadow public abstract boolean removeAllEffects();

    @Shadow public abstract boolean isInWall();

    @Inject(method = "checkTotemDeathProtection", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"),
            cancellable = true)
    public void improveTotemOfUndying(DamageSource pDamageSource, CallbackInfoReturnable<Boolean> cir) {
        if (BTUCommonConfigs.IS_MOD_ENABLED.get()) {
            boolean isRemoveAllEffectsEnabled = BTUCommonConfigs.REMOVE_ALL_EFFECTS.get();
            float health = BTUCommonConfigs.SET_HEALTH.get();

            if (isRemoveAllEffectsEnabled) {
                this.removeAllEffects();
            }

            this.setHealth(health);
            this.applyTotemEffects();
            this.increaseFoodLevel();
            this.destroyBlocksWhenSuffocatingOrFullyFrozen();
            this.knockBackMobsAway();

            this.level.broadcastEntityEvent(this, (byte) 35);
            cir.setReturnValue(true);
        }
    }

    private void applyTotemEffects(){
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
            if (this.isOnFire() && isFireResistanceEffectEnabled) {
                this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            }
            if (this.isInWaterOrBubble() && isWaterBreathingEffectEnabled) {
                this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
            }
        } else {
            if (isFireResistanceEffectEnabled) {
                this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
            }
            if (isWaterBreathingEffectEnabled) {
                this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
            }
        }

        if (isRegenerationEffectEnabled) {
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regenerationEffectDuration, regenerationEffectAmplifier));
        }
        if (isAbsorptionEffectEnabled) {
            this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, absorptionEffectDuration, absorptionEffectAmplifier));
        }
    }

    private void increaseFoodLevel(){
        boolean isIncreaseFoodLevelEnabled = BTUCommonConfigs.ENABLE_INCREASE_FOOD_LEVEL.get();
        int minimumFoodLevel = BTUCommonConfigs.MINIMUM_FOOD_LEVEL.get();
        int setFoodLevel = BTUCommonConfigs.SET_FOOD_LEVEL.get();

        if (this.getType() == EntityType.PLAYER && isIncreaseFoodLevelEnabled) {
            Player player = (Player) (Object) this;
            int foodLevel = player.getFoodData().getFoodLevel();
            if (foodLevel <= minimumFoodLevel) {
                player.getFoodData().setFoodLevel(setFoodLevel);
            }
        }
    }

    private void destroyBlocksWhenSuffocatingOrFullyFrozen(){
        boolean isDestroyBlocksWhenSuffocatingEnabled = BTUCommonConfigs.DESTROY_BLOCKS_WHEN_SUFFOCATING.get();
        boolean isDestroyPowderSnowWhenFullyFrozenEnabled = BTUCommonConfigs.DESTROY_POWDER_SNOW_WHEN_FULLY_FROZEN.get();

        if ((this.isInWall() && isDestroyBlocksWhenSuffocatingEnabled) || (this.isFullyFrozen() && isDestroyPowderSnowWhenFullyFrozenEnabled)) {
            Level level = this.level;
            BlockPos entityPosition = this.blockPosition();
            BlockState blockAtEntityPosition = level.getBlockState(entityPosition);
            BlockState blockAboveEntityPosition = level.getBlockState(entityPosition.above());

            if (blockAtEntityPosition.getBlock() != Blocks.BEDROCK && blockAboveEntityPosition.getBlock() != Blocks.BEDROCK) {
                if (level.getBlockState(entityPosition.above(2)).getBlock() instanceof FallingBlock) {
                    int i = 2;
                    while (true){
                        if (level.getBlockState(entityPosition.above(i)).getBlock() instanceof FallingBlock){
                            level.destroyBlock(entityPosition.above(i), true);
                            i++;
                        }else{
                            break;
                        }
                    }
                }
                level.destroyBlock(entityPosition, true);
                level.destroyBlock(entityPosition.above(), true);
            }
        }
    }

    private void knockBackMobsAway(){
        boolean isKnockBackMobsAwayEnabled = BTUCommonConfigs.KNOCK_BACK_MOBS_AWAY.get();
        double radius = BTUCommonConfigs.KNOCK_BACK_RADIUS.get();
        double strength = BTUCommonConfigs.KNOCK_BACK_STRENGTH.get();

        if (isKnockBackMobsAwayEnabled){
            AABB aabb = this.getBoundingBox().inflate(radius);
            List<LivingEntity> nearByEntities = this.level.getEntitiesOfClass(LivingEntity.class, aabb);

            for (LivingEntity entity : nearByEntities){
                if (!(entity instanceof Player)){
                    entity.knockback(strength, entity.getX() - this.getX(), entity.getZ() - this.getZ());
                }
            }
        }
    }
}