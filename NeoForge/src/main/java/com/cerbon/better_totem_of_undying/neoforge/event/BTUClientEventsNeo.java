package com.cerbon.better_totem_of_undying.neoforge.event;

import com.cerbon.better_totem_of_undying.config.BTUConfigs;
import com.cerbon.better_totem_of_undying.neoforge.client.RenderTotemOnChest;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = BTUConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BTUClientEventsNeo {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (MiscUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID) && BTUConstants.btuConfigs.charm.displayTotemOnChest)
            CuriosRendererRegistry.register(Items.TOTEM_OF_UNDYING, RenderTotemOnChest::new);

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> AutoConfig.getConfigScreen(BTUConfigs.class, parent).get()));
    }
}
