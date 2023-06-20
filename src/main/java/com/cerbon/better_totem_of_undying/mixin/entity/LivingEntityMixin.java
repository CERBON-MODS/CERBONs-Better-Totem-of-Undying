package com.cerbon.better_totem_of_undying.mixin.entity;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import com.cerbon.better_totem_of_undying.utils.BTUConstants;
import com.cerbon.better_totem_of_undying.utils.BTUUtils;
import com.cerbon.better_totem_of_undying.utils.ILivingEntityMixin;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Objects;

@Mixin(value = LivingEntity.class, priority = 1100)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityMixin {
    public long lastBlockPos;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow public abstract void setHealth(float pHealth);

    @Shadow public abstract boolean removeAllEffects();

    @Shadow public abstract boolean isInWall();

    @Shadow public abstract ItemStack getItemInHand(InteractionHand pHand);

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void checkTotemDeathProtection(DamageSource pDamageSource, CallbackInfoReturnable<Boolean> cir) {
        boolean isTeleportOutOfVoidEnabled = BTUCommonConfigs.TELEPORT_OUT_OF_VOID.get();
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        BlockPos entityPos = this.blockPosition();
        Level level = this.level;

        if (BTUUtils.isDimensionBlacklisted(level) || BTUUtils.isStructureBlacklisted(entityPos, (ServerLevel) level) || BTUUtils.damageBypassInvulnerability(pDamageSource, thisEntity) || (!isTeleportOutOfVoidEnabled && BTUUtils.isInVoid(thisEntity, pDamageSource)) || (thisEntity instanceof ServerPlayer serverPlayer && serverPlayer.getCooldowns().isOnCooldown(Items.TOTEM_OF_UNDYING))) {
            cir.setReturnValue(false);
        } else {
            boolean isUseTotemFromInventoryEnabled = BTUCommonConfigs.USE_TOTEM_FROM_INVENTORY.get();
            boolean isRemoveAllEffectsEnabled = BTUCommonConfigs.REMOVE_ALL_EFFECTS.get();
            boolean isCooldownEnabled = BTUCommonConfigs.ADD_COOLDOWN.get();
            float health = BTUCommonConfigs.SET_HEALTH.get();

            ItemStack itemstack = null;
            if (BTUUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID) && BTUCommonConfigs.USE_TOTEM_FROM_CHARM_SLOT.get()){
                ItemStack itemStack1 = CuriosApi.getCuriosHelper().findFirstCurio(thisEntity, Items.TOTEM_OF_UNDYING).map(SlotResult::stack).orElse(null);
                if (itemStack1 != null){
                    if (itemStack1.is(Items.TOTEM_OF_UNDYING)){
                        itemstack = itemStack1.copy();
                        itemStack1.shrink(1);
                    }
                }
            }
            if (thisEntity instanceof ServerPlayer serverPlayer && isUseTotemFromInventoryEnabled && itemstack == null){
                for (ItemStack itemstack1 : serverPlayer.getInventory().items){
                    if (itemstack1.is(Items.TOTEM_OF_UNDYING) && net.minecraftforge.common.ForgeHooks.onLivingUseTotem(thisEntity, pDamageSource, itemstack1, null)){
                        itemstack = itemstack1.copy();
                        itemstack1.shrink(1);
                        break;
                    }
                }
            }else if (itemstack == null){
                for(InteractionHand interactionhand : InteractionHand.values()) {
                    ItemStack itemstack1 = this.getItemInHand(interactionhand);
                    if (itemstack1.is(Items.TOTEM_OF_UNDYING) && net.minecraftforge.common.ForgeHooks.onLivingUseTotem(thisEntity, pDamageSource, itemstack1, interactionhand)) {
                        itemstack = itemstack1.copy();
                        itemstack1.shrink(1);
                        break;
                    }
                }
            }

            if (itemstack != null) {
                if (thisEntity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
                    CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, itemstack);

                    if (isCooldownEnabled){
                        serverPlayer.getCooldowns().addCooldown(Items.TOTEM_OF_UNDYING, BTUCommonConfigs.COOLDOWN.get());
                    }
                }

                if (isRemoveAllEffectsEnabled) {
                    this.removeAllEffects();
                }

                this.setHealth(health);
                BTUUtils.applyTotemEffects(thisEntity);
                BTUUtils.increaseFoodLevel(thisEntity);
                BTUUtils.destroyBlocksWhenSuffocatingOrFullyFrozen(thisEntity, level);
                BTUUtils.knockbackMobsAway(thisEntity, level);
                BTUUtils.teleportOutOfVoid(thisEntity, level, pDamageSource);

                level.broadcastEntityEvent(this, (byte) 35);
            }

            cir.setReturnValue(itemstack != null);
        }
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
        if (!this.level.isClientSide) {
            Level level = this.level;
            BlockPos currentPos = this.blockPosition();
            BlockState blockBelowEntityPos = level.getBlockState(currentPos.below());
            boolean isValidBlock = blockBelowEntityPos.isRedstoneConductor(level, currentPos.below());

            if (!Objects.equals(this.lastBlockPos, currentPos.asLong()) && isValidBlock) {
                this.lastBlockPos = currentPos.asLong();
            }
        }
    }

    @Override
    public long getLastBlockPos() {
        return lastBlockPos;
    }
}