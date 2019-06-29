package me.piggypiglet.gary.guice;

import com.google.inject.AbstractModule;

import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class ProviderSetterModule extends AbstractModule {
    private final Map<Class, Object> providers;

    public ProviderSetterModule(Map<Class, Object> providers) {
        this.providers = providers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure() {
        providers.forEach((c, o) -> bind(c).toInstance(o));
    }
}
