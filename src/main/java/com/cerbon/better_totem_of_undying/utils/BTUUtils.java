package com.cerbon.better_totem_of_undying.utils;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import net.minecraft.world.level.Level;

public class BTUUtils {
    public static boolean isDimensionBlacklisted(Level level){
        return BTUCommonConfigs.BLACKLISTED_DIMENSIONS.get().contains(level.dimension().location().toString());
    }
}
