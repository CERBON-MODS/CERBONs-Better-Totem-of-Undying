package com.cerbon.better_totem_of_undying.utils;

import net.minecraft.core.BlockPos;

public interface ILivingEntityMixin {
    void btu_setFallDamageImmune(boolean value);
    boolean btu_isFallDamageImmune();
    BlockPos btu_getLastBlockPos();
}
