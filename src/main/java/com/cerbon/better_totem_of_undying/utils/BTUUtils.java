package com.cerbon.better_totem_of_undying.utils;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.ArrayList;

public class BTUUtils {
    public static boolean isDimensionBlacklisted(Level level){
        return BTUCommonConfigs.BLACKLISTED_DIMENSIONS.get().contains(level.dimension().location().toString());
    }

    public static boolean isStructureBlacklisted(LivingEntity livingEntity, ServerLevel level){
        ArrayList<String> blackListedStructures = BTUCommonConfigs.BLACKLISTED_STRUCTURES.get();
        BlockPos blockPos = livingEntity.blockPosition();
        Registry<Structure> registry = livingEntity.getLevel().registryAccess().registryOrThrow(Registries.STRUCTURE);
        boolean flag = false;

        for (String structure : blackListedStructures){
            Structure structure1 = registry.get(new ResourceLocation(structure));

            if (structure1 != null){
                 if (level.structureManager().getStructureAt(blockPos, structure1).isValid()){
                     flag = true;
                 }
            }
        }
        return flag;
    }
}
