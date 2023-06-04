package com.cerbon.better_totem_of_undying.mixin.entity;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow public abstract boolean addEffect(MobEffectInstance pEffectInstance);

    @Shadow public abstract void setHealth(float pHealth);

    @Shadow public abstract boolean removeAllEffects();

    @Inject(method = "checkTotemDeathProtection", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V",
            ordinal = 0),
            cancellable = true)
    public void improveTotemOdUndying(DamageSource pDamageSource, CallbackInfoReturnable<Boolean> cir){
        if (BTUCommonConfigs.IS_MOD_ENABLED.get()){
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

            this.setHealth(BTUCommonConfigs.SET_HEALTH.get());

            if (BTUCommonConfigs.REMOVE_ALL_EFFECTS.get()){
                this.removeAllEffects();
            }

            if (BTUCommonConfigs.APPLY_EFFECTS_ONLY_WHEN_NEEDED.get()){
                if (isRegenerationEffectEnabled){
                    this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regenerationEffectDuration, regenerationEffectAmplifier));
                }
                if (isAbsorptionEffectEnabled){
                    this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, absorptionEffectDuration, absorptionEffectAmplifier));
                }
                if (this.isOnFire() && isFireResistanceEffectEnabled){
                    this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceEffectDuration, 0));
                }
                if (this.isInWaterOrBubble() && isWaterBreathingEffectEnabled){
                    this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, waterBreathingEffectDuration, 0));
                }

                this.level.broadcastEntityEvent(this, (byte)35);
            }
            cir.setReturnValue(true);
        }
    }
}
