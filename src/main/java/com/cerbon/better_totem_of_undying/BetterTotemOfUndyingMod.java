package com.cerbon.better_totem_of_undying;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(BetterTotemOfUndyingMod.MOD_ID)
public class BetterTotemOfUndyingMod
{
    public static final String MOD_ID = "better_totem_of_undying";

    public BetterTotemOfUndyingMod()
    {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BTUCommonConfigs.SPEC, "better_totem_of_undying.toml");
    }
}
