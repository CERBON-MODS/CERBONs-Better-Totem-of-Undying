package com.cerbon.better_totem_of_undying.forge;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import net.minecraftforge.fml.common.Mod;

@Mod(BTUConstants.MOD_ID)
public class BetterTotemOfUndyingForge {

    public BetterTotemOfUndyingForge() {
        BetterTotemOfUndying.init();
    }
}