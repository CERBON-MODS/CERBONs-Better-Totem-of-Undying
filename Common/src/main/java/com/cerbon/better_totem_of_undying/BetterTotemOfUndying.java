package com.cerbon.better_totem_of_undying;

import com.cerbon.better_totem_of_undying.config.BTUConfigs;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class BetterTotemOfUndying {
	public static BTUConfigs config;

	public static void init() {
		AutoConfig.register(BTUConfigs.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BTUConfigs.class).get();
	}
}
