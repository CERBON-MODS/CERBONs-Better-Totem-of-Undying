package com.cerbon.better_totem_of_undying.config.custom;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.List;

public class NewTotemFeatures {

    public boolean canUseTotemFromInventory = false;

    @Comment("Apply fire resistance and water breathing effects only when necessary. This means that you will receive these effects only if you are on fire or underwater, respectively.")
    public boolean applyEffectsOnlyWhenNecessary = true;

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

    @ConfigEntry.Gui.Excluded
    @Comment("Use this list to add custom effects and specify their triggers. Within this list, provide another list enclosed in curly braces. The first parameter should be the damage type, the second parameter the effect to be granted, the third parameter the duration of the effect in ticks, and the fourth parameter the effect amplifier. If you want the effect to be granted regardless of the damage type, use ‘any’. For example: [[\"minecraft:out_of_world\", \"minecraft:blindness\", \"800\", \"0\"]]. In this example, if the player dies in the void, the totem will grant a level 1 blindness effect with a duration of 40 seconds.")
    public List<List<String>> customEffects = List.of(List.of());

    public static class WaterBreathing {
        public boolean enabled = true;
        public int duration = 800;
    }

    public static class DestroyBlocks {
        public boolean whenSuffocating = true;
        public boolean whenFullyFrozen = true;

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
}
