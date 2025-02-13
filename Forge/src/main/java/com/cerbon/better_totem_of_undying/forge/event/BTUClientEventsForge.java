package com.cerbon.better_totem_of_undying.forge.event;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.config.BTUConfigs;
import com.cerbon.better_totem_of_undying.forge.client.RenderTotemOnChest;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = BTUConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BTUClientEventsForge {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (MiscUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID) && BetterTotemOfUndying.config.charm.displayTotemOnChest)
            CuriosRendererRegistry.register(Items.TOTEM_OF_UNDYING, RenderTotemOnChest::new);

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> AutoConfig.getConfigScreen(BTUConfigs.class, parent).get()));
    }
}
