package com.cerbon.better_totem_of_undying.config.custom;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class DefaultTotemFeatures {

    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    public int healthUponUse = 1;
    public boolean clearEffectsUponUse = true;

    @ConfigEntry.Gui.CollapsibleObject
    public FireResistance fireResistance = new FireResistance();

    @ConfigEntry.Gui.CollapsibleObject
    public Regeneration regeneration = new Regeneration();

    @ConfigEntry.Gui.CollapsibleObject
    public Absorption absorption = new Absorption();

    public static class FireResistance {
        public boolean enabled = true;
        public int duration = 800;
    }

    public static class Regeneration {
        public boolean enabled = true;
        public int duration = 900;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
        public int amplifier = 1;
    }

    public static class Absorption {
        public boolean enabled = true;
        public int duration  = 100;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
        public int amplifier  = 1;
    }
}
