package com.cerbon.better_totem_of_undying.mixin.entity;

import com.cerbon.better_totem_of_undying.utils.BTUUtils;
import com.cerbon.better_totem_of_undying.utils.ILivingEntityMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@SuppressWarnings(value = "AddedMixinMembersNamePattern")
@Mixin(value = LivingEntity.class, priority = 1100)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityMixin {

    @Unique private long lastBlockPos;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void checkTotemDeathProtection(DamageSource pDamageSource, @NotNull CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        cir.setReturnValue(BTUUtils.canSaveFromDeath(livingEntity, pDamageSource));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addCustomData(@NotNull CompoundTag pCompound, CallbackInfo ci){
        pCompound.putLong("LastBlockPos", this.lastBlockPos);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readCustomData(@NotNull CompoundTag pCompound, CallbackInfo ci){
        this.lastBlockPos = pCompound.getLong("LastBlockPos");
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    public void saveEntityLastBlockPos(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            Level level = this.level();
            BlockPos currentPos = this.blockPosition();
            BlockState blockBelowEntityPos = level.getBlockState(currentPos.below());
            boolean isValidBlock = blockBelowEntityPos.isRedstoneConductor(level, currentPos.below());

            if (!Objects.equals(this.lastBlockPos, currentPos.asLong()) && isValidBlock) {
                this.lastBlockPos = currentPos.asLong();
            }
        }
    }

    @Unique
    @Override
    public long getLastBlockPos() {
        return lastBlockPos;
    }
}