package me.piggypiglet.gary.registerables;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public abstract class Registerable {
    @Getter private final List<Object> providers = new ArrayList<>();

    protected abstract void execute();

    // TODO: provide injector back into registerable and inject members
    protected Registerable addProvider(Object instance) {
        providers.add(instance);
        return this;
    }

    public void run() {
        execute();
    }
}
