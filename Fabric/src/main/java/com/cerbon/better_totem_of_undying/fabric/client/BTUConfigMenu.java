package com.cerbon.better_totem_of_undying.fabric.client;

import com.cerbon.better_totem_of_undying.config.BTUConfigs;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public class BTUConfigMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(BTUConfigs.class, parent).get();
    }
}
