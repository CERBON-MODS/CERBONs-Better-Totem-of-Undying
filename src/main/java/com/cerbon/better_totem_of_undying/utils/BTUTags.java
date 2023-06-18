package com.cerbon.better_totem_of_undying.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BTUTags {
    public static final TagKey<Block> TOTEM_CANT_BREAK = BlockTags.create(new ResourceLocation(BTUConstants.MOD_ID, "totem_cant_break"));
}
