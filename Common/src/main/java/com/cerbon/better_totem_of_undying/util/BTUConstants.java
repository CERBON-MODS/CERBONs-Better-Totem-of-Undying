package com.cerbon.better_totem_of_undying.util;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;

public class BTUConstants {
    public static final String MOD_ID = "better_totem_of_undying";
    public static final String MOD_NAME = "Better Totem of Undying";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final TagKey<Block> TOTEM_CANT_DESTROY_TAG = TagKey.create(Registries.BLOCK, new ResourceLocation(BTUConstants.MOD_ID, "totem_cant_destroy"));

    public static final String CURIOS_MOD_ID = "curios";
    public static final String TRINKETS_MOD_ID = "trinkets";
}
