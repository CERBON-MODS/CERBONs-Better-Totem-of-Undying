package com.cerbon.better_totem_of_undying.client;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.platform.BTUServices;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderTotemOnChest {

    public static void render(LivingEntity livingEntity, EntityModel<? extends LivingEntity> model, PoseStack poseStack, ItemStack stack, MultiBufferSource buffer, int light) {
        var charmConfig = BetterTotemOfUndying.config.charm;

        poseStack.pushPose();

        BTUServices.PLATFORM_CHARM_HELPER.translateTotemPosition(livingEntity, model, poseStack);

        if (MiscUtils.getPlatformName().equals("Forge") || MiscUtils.getPlatformName().equals("NeoForge"))
            poseStack.translate(0.0F, 0.4F, -0.136F);
        else
            poseStack.translate(0.0F, 0.0F, 0.025F);

        poseStack.translate(charmConfig.xOffset, charmConfig.yOffset, charmConfig.zOffset);
        poseStack.scale(0.35F, 0.35F, 0.35F);
        poseStack.mulPose(Direction.DOWN.getRotation());

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, Minecraft.getInstance().level, 0);

        poseStack.popPose();
    }
}
