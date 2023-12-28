package com.cerbon.better_totem_of_undying.event;

import com.cerbon.better_totem_of_undying.utils.BTUConstants;
import com.cerbon.cerbons_api.api.static_utilities.MiscUtils;
import com.cerbon.cerbons_api.capability.providers.BlockPosHistoryProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mod.EventBusSubscriber(modid = BTUConstants.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player) || event.getObject().getCapability(BlockPosHistoryProvider.HISTORICAL_DATA).isPresent()) return;
        event.addCapability(new ResourceLocation(BTUConstants.MOD_ID, "block_pos_history"), new BlockPosHistoryProvider(5, true));
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesItemStack(AttachCapabilitiesEvent<ItemStack> event) {
        if (!MiscUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID) || !event.getObject().getItem().equals(Items.TOTEM_OF_UNDYING)) return;

        ItemStack itemStack = event.getObject();
        event.addCapability(new ResourceLocation(BTUConstants.MOD_ID, BTUConstants.CURIOS_MOD_ID), new ICapabilityProvider() {
            final ICurio curio = new ICurio() {

                @Override
                public boolean canEquipFromUse(SlotContext slotContext) {
                    return true;
                }

                @Override
                public ItemStack getStack() {
                    return itemStack;
                }
            };

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, LazyOptional.of(() -> curio));
            }
        });
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
