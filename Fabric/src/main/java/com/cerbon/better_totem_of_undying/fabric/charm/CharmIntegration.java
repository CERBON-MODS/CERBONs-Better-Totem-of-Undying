package com.cerbon.better_totem_of_undying.fabric.charm;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.client.RenderTotemOnChest;
import com.cerbon.better_totem_of_undying.config.BTUConfigs;
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

    public static void renderTotemOnChest() {
        TrinketRendererRegistry.registerRenderer(Items.TOTEM_OF_UNDYING, (stack, slotReference, contextModel, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch) ->
                RenderTotemOnChest.render(entity, contextModel, matrices, stack, vertexConsumers, light));
    }
}
