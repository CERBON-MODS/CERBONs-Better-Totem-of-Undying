package com.cerbon.better_totem_of_undying.event;

import com.cerbon.better_totem_of_undying.client.TotemCuriosRenderer;
import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import com.cerbon.better_totem_of_undying.utils.BTUConstants;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = BTUConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BTUClientEvents {

    @SubscribeEvent
    public static void clientSetupEvent(FMLClientSetupEvent event) {
        if (MiscUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID) && BTUCommonConfigs.DISPLAY_TOTEM_ON_CHEST.get())
            CuriosRendererRegistry.register(Items.TOTEM_OF_UNDYING, TotemCuriosRenderer::new);
    }
}
