package com.cerbon.better_totem_of_undying.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BTUCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Better Totem of Undying Configs");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
