package com.cerbon.better_totem_of_undying.fabric.charm;

import com.mojang.math.Axis;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;

public class CharmIntegration {

    public static void renderVoidTotemOnChest() {
        TrinketRendererRegistry.registerRenderer(Items.TOTEM_OF_UNDYING, (stack, slotReference, contextModel, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch) -> {

            if (entity instanceof AbstractClientPlayer player){
                TrinketRenderer.translateToChest(matrices, (PlayerModel<AbstractClientPlayer>) contextModel, player);

                matrices.scale(0.35F, 0.35F, 0.35F);
                matrices.mulPose(Axis.ZP.rotationDegrees(180));

                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, Minecraft.getInstance().level, 0);
            }
        });
    }
}
