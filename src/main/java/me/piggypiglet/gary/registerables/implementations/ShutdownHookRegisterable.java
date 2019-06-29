package me.piggypiglet.gary.registerables.implementations;

import com.google.inject.Inject;
import me.piggypiglet.gary.ShutdownHook;
import me.piggypiglet.gary.registerables.Registerable;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class ShutdownHookRegisterable extends Registerable {
    @Inject private ShutdownHook shutdownHook;

    @Override
    protected void execute() {
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
