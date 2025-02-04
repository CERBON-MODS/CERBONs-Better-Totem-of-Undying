package com.cerbon.better_totem_of_undying.config.custom;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

public class Blacklists {
    public List<Dimension> blacklistedDimensions = new ArrayList<>();
    public List<Structure> blacklistedStructures = new ArrayList<>();

    public static class Dimension {
        @Comment("The dimension to blacklist, e.g 'minecraft:overworld'.")
        public String name = "";
    }

    public static class Structure {
        @Comment("The structure to blacklist, e.g 'minecraft:end_city'")
        public String name = "";
    }
}
