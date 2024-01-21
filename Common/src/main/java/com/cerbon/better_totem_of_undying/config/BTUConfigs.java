package com.cerbon.better_totem_of_undying.config;

import com.cerbon.better_totem_of_undying.util.BTUConstants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(name = BTUConstants.MOD_ID)
public class BTUConfigs implements ConfigData {

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("This value sets the health Totem of Undying will give to the entity upon use. DEFAULT: 1")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
    public int SET_HEALTH = 1;

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("When Totem of Undying is used it removes all previous effects you had. If set to false, it will keep all the effects you had before using the totem. DEFAULT: TRUE")
    public boolean REMOVE_ALL_EFFECTS = true;

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("If false Totem of Undying will not give you fire resistance effect. DEFAULT: TRUE")
    public boolean ENABLE_FIRE_RESISTANCE = true;

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("Sets the duration of the fire resistance effect in ticks. DEFAULT: 800")
    public int FIRE_RESISTANCE_DURATION = 800;

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("If false Totem of Undying will not give you regeneration effect. DEFAULT: TRUE")
    public boolean ENABLE_REGENERATION = true;

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("Sets the duration of the regeneration effect in ticks. DEFAULT: 900")
    public int REGENERATION_DURATION = 900;

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("Sets the amplifier of the regeneration effect. DEFAULT: 1")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
    public int REGENERATION_AMPLIFIER = 1;

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("If false Totem of Undying will not give you absorption effect. DEFAULT: TRUE")
    public boolean ENABLE_ABSORPTION = true;

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("Sets the duration of the absorption effect in ticks. DEFAULT: 100")
    public int ABSORPTION_DURATION  = 100;

    @ConfigEntry.Category("minecraft_default_totem_features")
    @Comment("Sets the amplifier of the absorption effect. DEFAULT: 1")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 255)
    public int ABSORPTION_AMPLIFIER  = 1;


    @ConfigEntry.Category("new_totem_features")
    @Comment("If true you will be able to use the Totem of Undying from your inventory. DEFAULT: FALSE")
    public boolean USE_TOTEM_FROM_INVENTORY = false;

    @ConfigEntry.Category("new_totem_features")
    @Comment("Apply fire resistance and water breathing effects only when needed, meaning you are going to receive the effects only if you are on fire or in water respectively. DEFAULT: TRUE")
    public boolean APPLY_EFFECTS_ONLY_WHEN_NEEDED = true;

    @ConfigEntry.Category("new_totem_features")
    @Comment("If false Totem of Undying will not give you water breathing effect. DEFAULT: TRUE")
    public boolean ENABLE_WATER_BREATHING = true;

    @ConfigEntry.Category("new_totem_features")
    @Comment("Sets the duration of the water breathing effect in ticks. DEFAULT: 800")
    public int WATER_BREATHING_DURATION = 800;

    @ConfigEntry.Category("new_totem_features")
    @Comment("If false Totem of Undying will not give you slow falling effect. DEFAULT: TRUE")
    public boolean ENABLE_SLOW_FALLING = true;

    @ConfigEntry.Category("new_totem_features")
    @Comment("Sets the duration of the slow falling effect in ticks. DEFAULT: 600")
    public int SLOW_FALLING_DURATION = 600;

    @ConfigEntry.Category("new_totem_features")
    @ConfigEntry.Gui.Excluded
    @Comment("Use this list to add custom effects and specify when they should be triggered. Within this list, you should provide another list using curly braces, with the first parameter being the damage type, the second parameter being the effect that will be granted, the third parameter the duration of the effect in ticks, and the fourth parameter the effect amplifier. If you want the effect to always be granted, use the damage type as \"any\". Example: [[\"minecraft:out_of_world\", \"minecraft:blindness\", \"800\", \"0\"]]. In this example, if the player dies in the void, the totem will grant the level 1 blindness effect with a duration of 40 seconds. DEFAULT: Nothing")
    public List<List<String>> CUSTOM_EFFECTS = List.of();

    @ConfigEntry.Category("new_totem_features")
    @Comment("If false Totem of Undying will not increase your food level. DEFAULT: TRUE")
    public boolean ENABLE_INCREASE_FOOD_LEVEL = true;

    @ConfigEntry.Category("new_totem_features")
    @Comment("Sets the minimum food level needed to Totem of Undying increase food level. DEFAULT: <= 6")
    public int MINIMUM_FOOD_LEVEL = 6;

    @ConfigEntry.Category("new_totem_features")
    @Comment("Sets the food level that Totem of Undying will give upon use. DEFAULT: 8")
    public int SET_FOOD_LEVEL = 8;

    @ConfigEntry.Category("new_totem_features")
    @Comment("If false Totem of Undying will not break blocks when you are suffocating. DEFAULT: TRUE")
    public boolean DESTROY_BLOCKS_WHEN_SUFFOCATING = true;

    @ConfigEntry.Category("new_totem_features")
    @Comment("If false Totem of Undying will not break the powder snow when you die fully frozen. DEFAULT: TRUE")
    public boolean DESTROY_POWDER_SNOW_WHEN_FULLY_FROZEN = true;

    @ConfigEntry.Category("new_totem_features")
    @Comment("If false Totem of Undying will not knockback mobs away. DEFAULT: TRUE")
    public boolean KNOCKBACK_MOBS_AWAY = true;

    @ConfigEntry.Category("new_totem_features")
    @Comment("Sets the radius where entities needs to be for the totem knockback them. DEFAULT: 3.0")
    public double KNOCKBACK_RADIUS = 3.0D;

    @ConfigEntry.Category("new_totem_features")
    @Comment("Sets the strength of the knockback. DEFAULT: 2.5")
    public double KNOCKBACK_STRENGTH = 2.5D;

    @ConfigEntry.Category("new_totem_features")
    @Comment("If false Totem of Undying will not save you from dying in the void. DEFAULT: TRUE")
    public boolean TELEPORT_OUT_OF_VOID = true;

    @ConfigEntry.Category("new_totem_features")
    @Comment("If totem can't find a available position to teleport you back it will teleport you to the world's max build height plus this offset. DEFAULT:64")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 1024)
    public int TELEPORT_HEIGHT_OFFSET = 64;

    @ConfigEntry.Category("new_totem_features")
    @Comment("If true Totem of Undying will receive a cooldown after being used and you will not be able to use it again during this period. DEFAULT: FALSE")
    public boolean ADD_COOLDOWN = false;

    @ConfigEntry.Category("new_totem_features")
    @Comment("Sets the cooldown duration in ticks. DEFAULT: 200")
    public int COOLDOWN = 200;


    @ConfigEntry.Category("trinkets")
    @Comment("If false you will not be able to use Totem of Undying from trinkets slot (Trinkets mod must be installed). DEFAULT: TRUE")
    public boolean USE_TOTEM_FROM_TRINKETS_SLOT = true;

    @ConfigEntry.Category("trinkets")
    @Comment("If false Totem of Undying will not be displayed on the chest when worn in the trinkets charm slot (Trinkets mod must be installed). DEFAULT: TRUE")
    public boolean DISPLAY_TOTEM_ON_CHEST = true;


    @ConfigEntry.Category("blacklists")
    @Comment("You can put here dimensions where you don't want Totem of Undying to work. Example: \"minecraft:overworld\", \"mod_id:dimension_id\" DEFAULT: Nothing")
    public List<String> BLACKLISTED_DIMENSIONS = new ArrayList<>();

    @ConfigEntry.Category("blacklists")
    @Comment("You can put here structures where you don't want Totem of Undying to work. Example: \"minecraft:desert_pyramid\", \"mod_id:structure_id\" DEFAULT: Nothing")
    public List<String> BLACKLISTED_STRUCTURES = new ArrayList<>();
}