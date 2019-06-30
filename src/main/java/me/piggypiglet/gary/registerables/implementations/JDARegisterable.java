package me.piggypiglet.gary.registerables.implementations;

import com.google.inject.Inject;
import me.piggypiglet.gary.file.FileManager;
import me.piggypiglet.gary.registerables.Registerable;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class JDARegisterable extends Registerable {
    @Inject private FileManager fileManager;

    @Override
    protected void execute() {
        try {
            addBinding(new JDABuilder(fileManager.getConfig("config").getString("token"))
                    .setActivity(Activity.playing("testing"))
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}