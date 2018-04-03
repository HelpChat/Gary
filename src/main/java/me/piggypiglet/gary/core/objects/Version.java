package me.piggypiglet.gary.core.objects;

import com.google.inject.Singleton;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class Version {
    private final String version;

    public Version() {
        version = "@version@";
    }

    public String getVersion() {
        return version;
    }
}
