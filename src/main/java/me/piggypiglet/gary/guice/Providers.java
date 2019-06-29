package me.piggypiglet.gary.guice;

import java.util.Arrays;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public enum Providers {
    JDA(net.dv8tion.jda.api.JDA.class),
    UNKNOWN(null);

    private final Class clazz;

    Providers(Class clazz) {
        this.clazz = clazz;
    }

    public static Providers fromClass(Class clazz) {
        for (Providers provider : values()) {
            if (Arrays.asList(clazz.getInterfaces()).contains(provider.clazz)) {
                return provider;
            }
        }

        return UNKNOWN;
    }
}
