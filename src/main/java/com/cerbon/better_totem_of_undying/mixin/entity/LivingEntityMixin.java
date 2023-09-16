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

@Mixin(value = LivingEntity.class, priority = 1100)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityMixin {

    @Unique private long better_totem_of_undying_lastBlockPos;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void better_totem_of_undying_checkTotemDeathProtection(DamageSource pDamageSource, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        cir.setReturnValue(BTUUtils.canSaveFromDeath(livingEntity, pDamageSource));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void better_totem_of_undying_addCustomData(@NotNull CompoundTag pCompound, CallbackInfo ci){
        pCompound.putLong("LastBlockPos", this.better_totem_of_undying_lastBlockPos);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void better_totem_of_undying_readCustomData(@NotNull CompoundTag pCompound, CallbackInfo ci){
        this.better_totem_of_undying_lastBlockPos = pCompound.getLong("LastBlockPos");
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    public void better_totem_of_undying_saveEntityLastBlockPos(CallbackInfo ci) {
        if (!this.level.isClientSide) {
            Level level = this.level;
            BlockPos currentPos = this.blockPosition();
            BlockState blockBelowEntityPos = level.getBlockState(currentPos.below());
            boolean isValidBlock = blockBelowEntityPos.isRedstoneConductor(level, currentPos.below());

            if (!Objects.equals(this.better_totem_of_undying_lastBlockPos, currentPos.asLong()) && isValidBlock) {
                this.better_totem_of_undying_lastBlockPos = currentPos.asLong();
            }
        }
    }

    @Override
    public long better_totem_of_undying_getLastBlockPos() {
        return better_totem_of_undying_lastBlockPos;
    }
}