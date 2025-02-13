package com.cerbon.better_totem_of_undying.config.custom;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

public class NewTotemFeatures {

    public boolean canUseTotemFromInventory = false;

    @Comment("Apply fire resistance and water breathing effects only when necessary. This means that you will receive these effects only if you are on fire or underwater, respectively.")
    public boolean applyEffectsOnlyWhenNecessary = true;

    public List<CustomTotemEffect> customEffects = List.of();

    @ConfigEntry.Gui.CollapsibleObject
    public WaterBreathing waterBreathing = new WaterBreathing();

    @ConfigEntry.Gui.CollapsibleObject
    public DestroyBlocks destroyBlocks = new DestroyBlocks();

    @ConfigEntry.Gui.CollapsibleObject
    public KnockbackMobs knockbackMobs = new KnockbackMobs();

    @ConfigEntry.Gui.CollapsibleObject
    public Cooldown cooldown = new Cooldown();

    @ConfigEntry.Gui.CollapsibleObject
    public TeleportOutOfVoid teleportOutOfVoid = new TeleportOutOfVoid();

    @ConfigEntry.Gui.CollapsibleObject
    public TotemResistanceType totemResistanceType = new TotemResistanceType();

    public static class WaterBreathing {
        public boolean enabled = true;
        public int duration = 800;
    }

    public static class DestroyBlocks {
        public boolean whenSuffocating = true;
        public boolean whenFullyFrozen = true;

        public List<Block> totemCantDestroy = new ArrayList<>();
    }

    public static class Block {
        @Comment("The block that the Totem can't destroy, e.g. 'minecraft:stone'.")
        public String block = "";
    }

    public static class KnockbackMobs {
        public boolean enabled = true;
        public double radius = 3.0D;
        public double strength = 2.5D;
    }

    public static class Cooldown {
        public boolean enabled = false;
        public int cooldown = 200;
    }

    public static class TeleportOutOfVoid {
        public boolean enabled = true;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 1024)
        public int teleportHeightOffset = 64;
    }

    public static class CustomTotemEffect {

        @Comment("The mob effect to grant, e.g. 'minecraft:blindness'.")
        public String effect = "";

        @Comment("Duration of the effect in ticks (20 ticks = 1 second).")
        public int duration = 0;

        @Comment("Effect amplifier (0 = level 1, 1 = level 2, etc.).")
        public int amplifier = 0;

        @Comment("The damage type that triggers this effect. Use 'any' to always trigger, e.g. 'minecraft:out_of_world' or 'any'")
        public String damageType = "any";  //
    }

    public static class TotemResistanceType {
        @Comment("If true, totems with the Infinity enchantment will have unlimited uses (they won't be consumed)")
        public boolean infinity = false;
    }
}
