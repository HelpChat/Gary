package me.piggypiglet.gary.file.implementations.json.types;

import com.google.inject.Inject;
import me.piggypiglet.gary.file.framework.FileConfiguration;

import javax.annotation.Nonnull;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public enum Lang {
    STOP("commands.stop", "Shutting down."),
    RELOAD("commands.reload", "Reloaded all configuration files."),

    NO_PERMISSION("no-permission", "You do not have permission for that command.");

    private final String path;
    private final String def;

    Lang(String path, @Nonnull String def) {
        this.path = path;
        this.def = def;
    }

    public static String getMessage(Lang message, Object... variables) {
        return LanguageGuicer.get(message, variables);
    }

    public static class LanguageGuicer {
        @Inject @me.piggypiglet.gary.guice.annotations.Lang private static FileConfiguration lang;

        private static String get(Lang message, Object... variables) {
            String value = lang.getString(message.path, message.def);

            try {
                return String.format(value, variables);
            } catch (Exception e) {
                return value.replace("%s", "null");
            }
        }
    }
}