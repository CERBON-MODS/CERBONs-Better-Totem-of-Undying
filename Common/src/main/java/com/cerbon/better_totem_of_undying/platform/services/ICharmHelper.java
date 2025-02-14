package com.cerbon.better_totem_of_undying.platform.services;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ICharmHelper {
    @Nullable ItemStack getTotemFromCharmSlot(LivingEntity livingEntity);
    void translateTotemPosition(LivingEntity livingEntity, EntityModel<? extends LivingEntity> model, PoseStack poseStack);
}
