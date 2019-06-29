package me.piggypiglet.gary;

import com.google.inject.Injector;
import me.piggypiglet.gary.guice.InitializationModule;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class GaryBootstrap {
    private GaryBootstrap() {
        Injector injector = new InitializationModule(getClass()).createInjector();
        injector.getInstance(GaryBot.class).start(injector);
    }

    public static void main(String[] args) {
        new GaryBootstrap();
    }
}
