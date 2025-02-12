package com.cerbon.better_totem_of_undying.fabric;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.fabric.charm.CharmIntegration;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class BetterTotemOfUndyingFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        BetterTotemOfUndying.init();
    }

    @Override
    public void onInitializeClient() {
        if (MiscUtils.isModLoaded(BTUConstants.TRINKETS_MOD_ID) && BetterTotemOfUndying.config.charm.displayTotemOnChest)
            CharmIntegration.renderVoidTotemOnChest();
    }
}