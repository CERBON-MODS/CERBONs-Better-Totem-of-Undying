package com.cerbon.better_totem_of_undying.event;

import com.cerbon.better_totem_of_undying.utils.BTUConstants;
import com.cerbon.cerbons_api.capability.providers.BlockPosHistoryProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BTUConstants.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player) || event.getObject().getCapability(BlockPosHistoryProvider.HISTORICAL_DATA).isPresent()) return;
        event.addCapability(new ResourceLocation(BTUConstants.MOD_ID, "block_pos_history"), new BlockPosHistoryProvider(5, true));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) return;

        event.player.getCapability(BlockPosHistoryProvider.HISTORICAL_DATA).ifPresent(data -> {
            BlockPos previousPosition = data.get(0);
            BlockPos newPosition = event.player.blockPosition();
            Level level = event.player.level();
            boolean isValidBlock = level.getBlockState(newPosition.below()).isSolid();

            if (previousPosition != newPosition && isValidBlock)
                data.add(newPosition);
        });
    }
}
