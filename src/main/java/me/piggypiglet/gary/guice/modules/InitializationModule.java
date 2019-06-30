package me.piggypiglet.gary.guice.modules;

import com.google.inject.*;
import org.reflections.Reflections;

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

    @Provides
    @Singleton
    public Reflections providesReflections() {
        return new Reflections("me.piggypiglet.gary");
    }
}
