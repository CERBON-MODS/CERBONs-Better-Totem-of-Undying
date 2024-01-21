package com.cerbon.better_totem_of_undying.fabric.platform;

import com.cerbon.better_totem_of_undying.platform.services.ICharmHelper;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FabricCharmHelper implements ICharmHelper {

    @Override
    public @Nullable ItemStack getTotemFromCharmSlot(LivingEntity livingEntity) {
        if (MiscUtils.isModLoaded(BTUConstants.TRINKETS_MOD_ID) && BTUConstants.btuConfigs.charm.canUseTotemFromCharmSlot) {
            return TrinketsApi.getTrinketComponent(livingEntity).map(component -> {
                List<Tuple<SlotReference, ItemStack>> res = component.getEquipped(itemStack -> itemStack.is(Items.TOTEM_OF_UNDYING));
                return !res.isEmpty() ? res.get(0).getB() : null;
            }).orElse(null);
        }
        return null;
    }
}
