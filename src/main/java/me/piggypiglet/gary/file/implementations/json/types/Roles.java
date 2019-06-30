package me.piggypiglet.gary.file.implementations.json.types;

import com.google.inject.Inject;
import me.piggypiglet.gary.file.framework.FileConfiguration;

import java.util.ArrayList;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class Roles {
    @Inject @me.piggypiglet.gary.guice.annotations.Roles private static FileConfiguration roles;

    public static boolean hasPermission(String id, String permission) {
        return roles.getStringList(id, new ArrayList<>()).contains(permission);
    }
}