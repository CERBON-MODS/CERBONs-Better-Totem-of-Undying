package com.cerbon.better_totem_of_undying.platform.services;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ICharmHelper {
    @Nullable ItemStack getTotemFromCharmSlot(LivingEntity livingEntity);
}
