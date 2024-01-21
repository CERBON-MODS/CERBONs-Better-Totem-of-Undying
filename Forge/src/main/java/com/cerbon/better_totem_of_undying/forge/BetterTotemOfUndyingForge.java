package com.cerbon.better_totem_of_undying.forge;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.util.BMDConstants;
import net.minecraftforge.fml.common.Mod;

@Mod(BMDConstants.MOD_ID)
public class BetterTotemOfUndyingForge {

    public BetterTotemOfUndyingForge() {
        BetterTotemOfUndying.init();
    }
}