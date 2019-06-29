package me.piggypiglet.gary.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class InitializationModule extends AbstractModule {
    private final Class main;

    public InitializationModule(Class main) {
        this.main = main;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure() {
        bind(main).toInstance(main);
    }
}
