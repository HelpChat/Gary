package me.piggypiglet.gary.registerables.implementations;

import com.google.inject.Inject;
import me.piggypiglet.gary.file.FileManager;
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
            fileManager.load("config", "/config.json", "./config.json");
            fileManager.load("embed", "/embed.json", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
