package com.cerbon.better_totem_of_undying.mixin.entity;


import com.cerbon.better_totem_of_undying.util.BTUUtils;
import com.cerbon.better_totem_of_undying.util.mixin.ILivingEntityMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 1100)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityMixin {
    @Unique private boolean btu_isFallDamageImmune;
    @Unique private long btu_lastBlockPos;

    private LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void btu_checkTotemDeathProtection(DamageSource damageSource, @NotNull CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        cir.setReturnValue(BTUUtils.canSaveFromDeath(livingEntity, damageSource));
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void btu_causeFallDamage(float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        if (((ILivingEntityMixin) livingEntity).btu_isFallDamageImmune()) {
            btu_setFallDamageImmune(false);
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void btu_tick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        BTUUtils.resetFallDamageImmune(livingEntity);

        BlockPos previousPosition = btu_getLastBlockPos();
        BlockPos newPosition = livingEntity.blockPosition();
        Level level = livingEntity.level();
        boolean isValidBlock = level.getBlockState(newPosition.below()).isSolid();

        if (previousPosition != newPosition && isValidBlock)
            this.btu_lastBlockPos = newPosition.asLong();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void btu_addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("BTUIsFallDamageImmune", this.btu_isFallDamageImmune);
        tag.putLong("BTULastBlockPos", this.btu_lastBlockPos);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void btu_readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.btu_isFallDamageImmune = tag.getBoolean("BTUIsFallDamageImmune");
        this.btu_lastBlockPos = tag.getLong("BTULastBlockPos");
    }

    @Override
    public void btu_setFallDamageImmune(boolean value) {
        this.btu_isFallDamageImmune = value;
    }

    @Override
    public boolean btu_isFallDamageImmune() {
        return this.btu_isFallDamageImmune;
    }

    @Override
    public BlockPos btu_getLastBlockPos() {
        return BlockPos.of(this.btu_lastBlockPos);
    }
}
