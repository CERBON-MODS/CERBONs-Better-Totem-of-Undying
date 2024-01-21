package com.cerbon.better_totem_of_undying.platform;

import com.cerbon.better_totem_of_undying.util.BMDConstants;

import java.util.ServiceLoader;

public class BMDServices {

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        BMDConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
