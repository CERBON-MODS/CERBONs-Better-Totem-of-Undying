package com.cerbon.better_totem_of_undying.fabric;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;

public class BetterTotemOfUndyingFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        BetterTotemOfUndying.init();
    }

    @Override
    public void onInitializeClient() {
        if (MiscUtils.isModLoaded(BTUConstants.TRINKETS_MOD_ID) && BetterTotemOfUndying.config.charm.displayTotemOnChest)
            renderVoidTotemOnChest();
    }

    private void renderVoidTotemOnChest() {
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