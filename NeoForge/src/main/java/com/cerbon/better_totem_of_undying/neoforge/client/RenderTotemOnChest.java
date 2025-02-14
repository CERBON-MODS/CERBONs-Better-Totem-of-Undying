package com.cerbon.better_totem_of_undying.neoforge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class RenderTotemOnChest implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack itemStack, @NotNull SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource multiBufferSource, int i, float v, float v1, float v2, float v3, float v4, float v5) {
        com.cerbon.better_totem_of_undying.client.RenderTotemOnChest.render(slotContext.entity(), renderLayerParent.getModel(), poseStack, itemStack, multiBufferSource, i);
    }
}
