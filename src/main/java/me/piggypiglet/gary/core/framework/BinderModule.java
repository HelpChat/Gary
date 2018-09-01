package me.piggypiglet.gary.core.framework;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class BinderModule extends AbstractModule {
    private final Class clazz;

    public BinderModule(Class clazz) {
        this.clazz = clazz;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure() {
        this.bind(clazz).toInstance(clazz);
    }
}
