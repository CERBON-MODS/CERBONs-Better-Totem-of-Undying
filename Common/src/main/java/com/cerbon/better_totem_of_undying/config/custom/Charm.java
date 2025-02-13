package com.cerbon.better_totem_of_undying.config.custom;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class Charm {
    public boolean canUseTotemFromCharmSlot = true;
    @ConfigEntry.Gui.RequiresRestart
    public boolean displayTotemOnChest = true;

    @Comment("The X offset for rendering the totem on the player")
    public float xOffset = 0;
    @Comment("The Y offset for rendering the totem on the player")
    public float yOffset = 0;
    @Comment("The Z offset for rendering the totem on the player")
    public float zOffset = 0;
}
