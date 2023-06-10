package com.cerbon.better_totem_of_undying.utils;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.ArrayList;

public class BTUUtils {

    //The method also checks the entity height to be sure it's not in the void. That way it doesn't conflict with the ability that saves the entity from dying in the void.
    public static boolean damageBypassInvulnerability(DamageSource damageSource, LivingEntity livingEntity){
        return damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !(livingEntity.getY() < livingEntity.level.getMinBuildHeight());
    }

    public static boolean isDimensionBlacklisted(Level level){
        return BTUCommonConfigs.BLACKLISTED_DIMENSIONS.get().contains(level.dimension().location().toString());
    }

    public static boolean isStructureBlacklisted(BlockPos pos, ServerLevel level){
        ArrayList<String> blackListedStructures = BTUCommonConfigs.BLACKLISTED_STRUCTURES.get();
        Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        boolean flag = false;
        for (String structureName : blackListedStructures){
            Structure structure = structureRegistry.get(new ResourceLocation(structureName));

            if (structure != null){
                 if (level.structureManager().getStructureAt(pos, structure).isValid()){
                     flag = true;
                 }
            }
        }
        return flag;
    }
}
