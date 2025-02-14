package com.cerbon.better_totem_of_undying.neoforge.platform;

import com.cerbon.better_totem_of_undying.BetterTotemOfUndying;
import com.cerbon.better_totem_of_undying.platform.services.ICharmHelper;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class NeoCharmHelper implements ICharmHelper {

    @Override
    public @Nullable ItemStack getTotemFromCharmSlot(LivingEntity livingEntity) {
        if (MiscUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID) && BetterTotemOfUndying.config.charm.canUseTotemFromCharmSlot)
            return CuriosApi.getCuriosHelper().findFirstCurio(livingEntity, Items.TOTEM_OF_UNDYING).map(SlotResult::stack).orElse(null);

        return null;
    }

    @Override
    public void translateTotemPosition(LivingEntity livingEntity, EntityModel<? extends LivingEntity> model, PoseStack poseStack) {
        ICurioRenderer.translateIfSneaking(poseStack, livingEntity);
        ICurioRenderer.rotateIfSneaking(poseStack, livingEntity);
    }
}
