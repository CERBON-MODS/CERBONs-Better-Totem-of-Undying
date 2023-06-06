package com.cerbon.better_totem_of_undying.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BTUCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> IS_MOD_ENABLED;
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
    public static final ForgeConfigSpec.ConfigValue<Float> SET_HEALTH;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_INCREASE_FOOD_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DESTROY_BLOCKS_WHEN_SUFFOCATING;
    public static final ForgeConfigSpec.ConfigValue<Integer> MINIMUM_FOOD_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Integer> SET_FOOD_LEVEL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> REMOVE_ALL_EFFECTS;

    static {
        BUILDER.push("Better Totem of Undying Configs");
        IS_MOD_ENABLED = BUILDER
                .comment("If false the mod will not change Minecraft Totem of Undying. DEFAULT: TRUE")
                .define("Is Mod Enabled", true);

        SET_HEALTH = BUILDER
                .comment("This value sets the health Totem of Undying will give to the entity upon use. DEFAULT: 1.0")
                .define("Set Health", 1.0F);

        ENABLE_INCREASE_FOOD_LEVEL = BUILDER
                .comment("If false Totem Of Undying will not increase your food level. DEFAULT: TRUE")
                .define("Increase Food Level", true);

        MINIMUM_FOOD_LEVEL = BUILDER
                .comment("Sets the minimum food level needed to Totem Of Undying increase food level. DEFAULT: <= 6")
                .define("Minimum Food Level", 6);

        SET_FOOD_LEVEL = BUILDER
                .comment("Sets the food level that Totem Of Undying will give upon use. DEFAULT: 10 = Half bar")
                .define("Set Food Level", 10);

        DESTROY_BLOCKS_WHEN_SUFFOCATING = BUILDER
                .comment("If false Totem Of Undying will not break blocks when you are suffocating. DEFAULT: TRUE")
                .define("Destroy Blocks When Suffocating", true);

        REMOVE_ALL_EFFECTS = BUILDER
                .comment("When Totem of Undying is used it removes all previous effects you had. If set to false, it will keep all the effects you had before using the totem. DEFAULT: TRUE")
                .define("Remove All Effects", true);


        BUILDER.push("Effects");
        APPLY_EFFECTS_ONLY_WHEN_NEEDED = BUILDER
                .comment("If false it will apply all the effects at once, example: if you are not in fire you will receive the fire resistance effect but if it's set to true you will only receive the effect if you are in fire. DEFAULT: TRUE")
                .define("Apply Effects Only When Needed", true);

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
                .define("Regeneration Amplifier", 1);

        ENABLE_ABSORPTION = BUILDER
                .comment("If false Totem of Undying will not give you absorption effect. DEFAULT: TRUE")
                .define("Enable Absorption", true);

        ABSORPTION_DURATION = BUILDER
                .comment("Sets the duration of the absorption effect in ticks. DEFAULT: 100")
                .define("Absorption Duration", 100);

        ABSORPTION_AMPLIFIER = BUILDER
                .comment("Sets the amplifier of the absorption effect. DEFAULT: 1")
                .define("Absorption Amplifier", 1);

        ENABLE_WATER_BREATHING = BUILDER
                .comment("If false Totem of Undying will not give you water breathing effect. DEFAULT: TRUE")
                .define("Enable Water Breathing", true);

        WATER_BREATHING_DURATION= BUILDER
                .comment("Sets the duration of the water breathing effect in ticks. DEFAULT: 800")
                .define("Water Breathing Duration", 800);
        BUILDER.pop();


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
