package com.cerbon.better_totem_of_undying.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;

public class BTUCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> USE_TOTEM_FROM_INVENTORY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> APPLY_EFFECTS_ONLY_WHEN_NEEDED;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_FIRE_RESISTANCE;
    public static final ForgeConfigSpec.ConfigValue<Integer> FIRE_RESISTANCE_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_REGENERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> REGENERATION_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> REGENERATION_AMPLIFIER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ABSORPTION;
    public static final ForgeConfigSpec.ConfigValue<Integer> ABSORPTION_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> ABSORPTION_AMPLIFIER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_WATER_BREATHING;
    public static final ForgeConfigSpec.ConfigValue<Integer> WATER_BREATHING_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SLOW_FALLING;
    public static final ForgeConfigSpec.ConfigValue<Integer> SLOW_FALLING_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Float> SET_HEALTH;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_INCREASE_FOOD_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DESTROY_BLOCKS_WHEN_SUFFOCATING;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DESTROY_POWDER_SNOW_WHEN_FULLY_FROZEN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> KNOCKBACK_MOBS_AWAY;
    public static final ForgeConfigSpec.ConfigValue<Double> KNOCKBACK_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Double> KNOCKBACK_STRENGTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> MINIMUM_FOOD_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Integer> SET_FOOD_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> REMOVE_ALL_EFFECTS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TELEPORT_OUT_OF_VOID;
    public static final ForgeConfigSpec.ConfigValue<Integer> TELEPORT_HEIGHT_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ADD_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<ArrayList<String>> BLACKLISTED_DIMENSIONS;
    public static final ForgeConfigSpec.ConfigValue<ArrayList<String>> BLACKLISTED_STRUCTURES;

    static {
        BUILDER.push("Default Totem Abilities from Minecraft");
        SET_HEALTH = BUILDER
                .comment("This value sets the health Totem of Undying will give to the entity upon use. DEFAULT: 1.0")
                .define("Set Health", 1.0F);

        REMOVE_ALL_EFFECTS = BUILDER
                .comment("When Totem of Undying is used it removes all previous effects you had. If set to false, it will keep all the effects you had before using the totem. DEFAULT: TRUE")
                .define("Remove All Effects", true);

        BUILDER.push("Effects");
        ENABLE_FIRE_RESISTANCE = BUILDER
                .comment("If false Totem of Undying will not give you fire resistance effect. DEFAULT: TRUE")
                .define("Enable Fire Resistance", true);

        FIRE_RESISTANCE_DURATION = BUILDER
                .comment("Sets the duration of the fire resistance effect in ticks. DEFAULT: 800")
                .define("Fire Resistance Duration", 800);

        ENABLE_REGENERATION = BUILDER
                .comment("If false Totem of Undying will not give you regeneration effect. DEFAULT: TRUE")
                .define("Enable Regeneration", true);

        REGENERATION_DURATION = BUILDER
                .comment("Sets the duration of the regeneration effect in ticks. DEFAULT: 900")
                .define("Regeneration Duration", 900);

        REGENERATION_AMPLIFIER = BUILDER
                .comment("Sets the amplifier of the regeneration effect. DEFAULT: 1")
                .defineInRange("Regeneration Amplifier", 1, 0, 255);

        ENABLE_ABSORPTION = BUILDER
                .comment("If false Totem of Undying will not give you absorption effect. DEFAULT: TRUE")
                .define("Enable Absorption", true);

        ABSORPTION_DURATION = BUILDER
                .comment("Sets the duration of the absorption effect in ticks. DEFAULT: 100")
                .define("Absorption Duration", 100);

        ABSORPTION_AMPLIFIER = BUILDER
                .comment("Sets the amplifier of the absorption effect. DEFAULT: 1")
                .defineInRange("Absorption Amplifier", 1, 0, 255);
        BUILDER.pop(2);


        BUILDER.push("New Totem Abilities");

        USE_TOTEM_FROM_INVENTORY = BUILDER
                .comment("If true you will be able to use the Totem of Undying from your inventory. DEFAULT: FALSE")
                .define("Use Totem From Inventory", false);

        BUILDER.push("Effects");
        APPLY_EFFECTS_ONLY_WHEN_NEEDED = BUILDER
                .comment("If false it will apply all the effects at once, example: if you are not on fire you will receive the fire resistance effect but if it's set to true you will only receive the effect if you are on fire. DEFAULT: TRUE")
                .define("Apply Effects Only When Needed", true);

        ENABLE_WATER_BREATHING = BUILDER
                .comment("If false Totem of Undying will not give you water breathing effect. DEFAULT: TRUE")
                .define("Enable Water Breathing", true);

        WATER_BREATHING_DURATION= BUILDER
                .comment("Sets the duration of the water breathing effect in ticks. DEFAULT: 800")
                .define("Water Breathing Duration", 800);

        ENABLE_SLOW_FALLING = BUILDER
                .comment("If false Totem of Undying will not give you slow falling effect. DEFAULT: TRUE")
                .define("Enable Slow Falling", true);

        SLOW_FALLING_DURATION = BUILDER
                .comment("Sets the duration of the slow falling effect in ticks. DEFAULT: 600")
                .define("Slow Falling Duration", 600);
        BUILDER.pop();

        BUILDER.push("Increase food level");
        ENABLE_INCREASE_FOOD_LEVEL = BUILDER
                .comment("If false Totem of Undying will not increase your food level. DEFAULT: TRUE")
                .define("Increase Food Level", true);

        MINIMUM_FOOD_LEVEL = BUILDER
                .comment("Sets the minimum food level needed to Totem of Undying increase food level. DEFAULT: <= 6")
                .defineInRange("Minimum Food Level", 6, 0 , 20);

        SET_FOOD_LEVEL = BUILDER
                .comment("Sets the food level that Totem of Undying will give upon use. DEFAULT: 8")
                .defineInRange("Set Food Level", 8, 0, 20);
        BUILDER.pop();

        BUILDER.push("Destroy Blocks When Suffocating or Fully Frozen");
        DESTROY_BLOCKS_WHEN_SUFFOCATING = BUILDER
                .comment("If false Totem of Undying will not break blocks when you are suffocating. DEFAULT: TRUE")
                .define("Destroy Blocks When Suffocating", true);

        DESTROY_POWDER_SNOW_WHEN_FULLY_FROZEN = BUILDER
                .comment("If false Totem of Undying will not break the powder snow when you die fully frozen. DEFAULT: TRUE")
                .define("Destroy Powder Snow When Fully Frozen", true);
        BUILDER.pop();

        BUILDER.push("Knockback Mobs Away");
        KNOCKBACK_MOBS_AWAY = BUILDER
                .comment("If false Totem of Undying will not knockback mobs away. DEFAULT: TRUE")
                .define("Knockback Mobs Away", true);

        KNOCKBACK_RADIUS = BUILDER
                .comment("Sets the radius where entities needs to be for the totem knockback them. DEFAULT: 3.0")
                .define("Knockback Radius", 3.0D);

        KNOCKBACK_STRENGTH = BUILDER
                .comment("Sets the strength of the knockback. DEFAULT: 2.5")
                .define("Knockback Strength", 2.5D);
        BUILDER.pop();

        BUILDER.push("Teleport Out of Void");
        TELEPORT_OUT_OF_VOID = BUILDER
                .comment("If false Totem of Undying will not save you from dying in the void. DEFAULT: TRUE")
                .define("Teleport Out of Void", true);

        TELEPORT_HEIGHT_OFFSET = BUILDER
                .comment("If totem can't find a available position to teleport you back it will teleport you to the world's max build height plus this offset. DEFAULT:64")
                .defineInRange("Teleport Height Offset", 64, 0, 1024);
        BUILDER.pop();

        BUILDER.push("Add Cooldown");
        ADD_COOLDOWN = BUILDER
                .comment("If true Totem of Undying will receive a cooldown after being used and you will not be able to use it again during this period. DEFAULT: FALSE")
                .define("Add Cooldown", false);

        COOLDOWN = BUILDER
                .comment("Sets the cooldown duration in ticks. DEFAULT: 200")
                .define("Cooldown", 200);

        BUILDER.pop(2);

        BUILDER.push("Blacklists");
        BLACKLISTED_DIMENSIONS = BUILDER
                .comment("You can put here dimensions where you don't want the Totem of Undying to work. Example: \"minecraft:overworld\", \"mod_id:dimension_id\" DEFAULT: Nothing")
                .define("Blacklisted Dimensions", new ArrayList<>());

        BLACKLISTED_STRUCTURES = BUILDER
                .comment("You can put here structures where you don't want the Totem of Undying to work. Example: \"minecraft:desert_pyramid\", \"mod_id:structure_id\" DEFAULT: Nothing")
                .define("Blacklisted Structures", new ArrayList<>());

        SPEC = BUILDER.build();
    }
}
