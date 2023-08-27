package com.cerbon.better_totem_of_undying;

import com.cerbon.better_totem_of_undying.client.TotemCuriosRenderer;
import com.cerbon.better_totem_of_undying.config.BTUCommonConfigs;
import com.cerbon.better_totem_of_undying.utils.BTUConstants;
import com.cerbon.better_totem_of_undying.utils.BTUUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;


@Mod(BTUConstants.MOD_ID)
public class BetterTotemOfUndying {
    public static final Logger LOGGER = LogUtils.getLogger();

    public BetterTotemOfUndying() {
        if (BTUUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID)){
            InterModComms.sendTo(BTUConstants.CURIOS_MOD_ID, SlotTypeMessage.REGISTER_TYPE, ()-> SlotTypePreset.CHARM.getMessageBuilder().build());
            BetterTotemOfUndying.LOGGER.debug("Enqueued IMC to {}", BTUConstants.CURIOS_MOD_ID);
        }

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addGenericListener(ItemStack.class, this::attachCaps);
        forgeEventBus.register(this);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetupEvent);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BTUCommonConfigs.SPEC, BTUConstants.CONFIG_NAME);
    }

    private void attachCaps(AttachCapabilitiesEvent<ItemStack> event){
        if (BTUUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID)){
            ItemStack itemStack = event.getObject();
            Item item = itemStack.getItem();

            if (item.equals(Items.TOTEM_OF_UNDYING)){
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
                        return CuriosCapability.ITEM.orEmpty(cap, LazyOptional.of(()-> curio));
                    }
                });
            }
        }
    }

    private void clientSetupEvent(FMLClientSetupEvent event){
        if (BTUUtils.isModLoaded(BTUConstants.CURIOS_MOD_ID) && BTUCommonConfigs.DISPLAY_TOTEM_ON_CHEST.get()){
            CuriosRendererRegistry.register(Items.TOTEM_OF_UNDYING, TotemCuriosRenderer::new);
        }
    }
}
