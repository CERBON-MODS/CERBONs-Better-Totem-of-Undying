package com.cerbon.better_totem_of_undying.platform;

import com.cerbon.better_totem_of_undying.platform.services.ICharmHelper;
import com.cerbon.better_totem_of_undying.util.BTUConstants;

import java.util.ServiceLoader;

public class BTUServices {
    public static final ICharmHelper PLATFORM_CHARM_HELPER = load(ICharmHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        BTUConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
