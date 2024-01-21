package com.cerbon.better_totem_of_undying.config;

import com.cerbon.better_totem_of_undying.config.custom.Blacklists;
import com.cerbon.better_totem_of_undying.config.custom.Curios;
import com.cerbon.better_totem_of_undying.config.custom.DefaultTotemFeatures;
import com.cerbon.better_totem_of_undying.config.custom.NewTotemFeatures;
import com.cerbon.better_totem_of_undying.util.BTUConstants;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = BTUConstants.MOD_ID)
public class BTUConfigs implements ConfigData {

    @ConfigEntry.Category("Default Totem Features")
    @ConfigEntry.Gui.TransitiveObject
    public DefaultTotemFeatures defaultTotemFeatures = new DefaultTotemFeatures();

    @ConfigEntry.Category("New Totem Features")
    @ConfigEntry.Gui.TransitiveObject
    public NewTotemFeatures newTotemFeatures = new NewTotemFeatures();

    @ConfigEntry.Category("Curios")
    @ConfigEntry.Gui.TransitiveObject
    public Curios curios = new Curios();

    @ConfigEntry.Category("Blacklists")
    @ConfigEntry.Gui.TransitiveObject
    public Blacklists blacklists = new Blacklists();
}