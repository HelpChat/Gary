package me.piggypiglet.gary.registerables.implementations;

import com.google.inject.Inject;
import me.piggypiglet.gary.file.FileManager;
import me.piggypiglet.gary.file.framework.FileConfiguration;
import me.piggypiglet.gary.guice.annotations.Config;
import me.piggypiglet.gary.guice.annotations.Embed;
import me.piggypiglet.gary.guice.annotations.Lang;
import me.piggypiglet.gary.guice.annotations.Roles;
import me.piggypiglet.gary.registerables.Registerable;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class FilesRegisterable extends Registerable {
    @Inject private FileManager fileManager;

    @Override
    protected void execute() {
        try {
            addAnnotatedBinding(FileConfiguration.class, Config.class, fileManager.load("config", "/config.json", "./config.json"));
            addAnnotatedBinding(FileConfiguration.class, Lang.class, fileManager.load("lang", "/lang.json", "./lang.json"));
            addAnnotatedBinding(FileConfiguration.class, Roles.class, fileManager.load("roles", "/roles.json", "./roles.json"));
            addAnnotatedBinding(FileConfiguration.class, Embed.class, fileManager.load("embed", "/embed.json", null));
            requestStaticInjections(me.piggypiglet.gary.file.implementations.json.types.Roles.class, me.piggypiglet.gary.file.implementations.json.types.Lang.LanguageGuicer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}