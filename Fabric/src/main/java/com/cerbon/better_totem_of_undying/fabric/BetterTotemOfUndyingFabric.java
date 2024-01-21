package com.cerbon.better_totem_of_undying.fabric;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class BetterTotemOfUndyingFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        BetterTotemOfUndying.init();
    }

    @Override
    public void onInitializeClient() {}
}