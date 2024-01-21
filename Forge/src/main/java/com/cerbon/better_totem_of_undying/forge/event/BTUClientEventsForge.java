package com.cerbon.better_totem_of_undying.forge.event;

import com.cerbon.better_totem_of_undying.config.BTUConfigs;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BTUConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BTUClientEventsForge {

    @SubscribeEvent
    protected static void onClientSetup(FMLClientSetupEvent event){
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> AutoConfig.getConfigScreen(BTUConfigs.class, parent).get()));
    }
}
