package com.cerbon.better_totem_of_undying.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BTUConstants {
    public static final String MOD_ID = "better_totem_of_undying";
    public static final String CONFIG_NAME = MOD_ID + ".toml";

    public static final TagKey<Block> TOTEM_CANT_DESTROY_TAG = BlockTags.create(new ResourceLocation(BTUConstants.MOD_ID, "totem_cant_destroy"));
}
