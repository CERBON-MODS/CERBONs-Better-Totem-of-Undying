package com.cerbon.better_totem_of_undying;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import com.cerbon.better_totem_of_undying.utils.BTUConstants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(BTUConstants.MOD_ID)
public class BetterTotemOfUndyingMod {

    public BetterTotemOfUndyingMod() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BTUCommonConfigs.SPEC, BTUConstants.CONFIG_NAME);
    }
}
