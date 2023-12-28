package com.cerbon.better_totem_of_undying;

import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import com.cerbon.better_totem_of_undying.utils.BTUConstants;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;


@Mod(BTUConstants.MOD_ID)
public class BetterTotemOfUndying {
    public static final Logger LOGGER = LogUtils.getLogger();

    public BetterTotemOfUndying() {
        if (MiscUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID)) {
            InterModComms.sendTo(BTUConstants.CURIOS_MOD_ID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
            BetterTotemOfUndying.LOGGER.debug("Enqueued IMC to {}", BTUConstants.CURIOS_MOD_ID);
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BTUCommonConfigs.SPEC, BTUConstants.CONFIG_NAME);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
