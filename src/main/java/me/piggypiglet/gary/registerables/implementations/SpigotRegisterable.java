package me.piggypiglet.gary.registerables.implementations;

import be.maximvdw.spigotsite.SpigotSiteCore;
import be.maximvdw.spigotsite.api.SpigotSiteAPI;
import be.maximvdw.spigotsite.api.user.User;
import com.google.inject.Inject;
import me.piggypiglet.gary.file.framework.FileConfiguration;
import me.piggypiglet.gary.guice.annotations.Config;
import me.piggypiglet.gary.registerables.Registerable;
import org.slf4j.LoggerFactory;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class SpigotRegisterable extends Registerable {
    @Inject @Config private FileConfiguration config;

    @Override
    protected void execute() {
        LoggerFactory.getLogger("SpigotAPI").info("Initializing, this can take a while.");
        SpigotSiteAPI api = new SpigotSiteCore();
        addBinding(SpigotSiteAPI.class, api);

        try {
            addBinding(User.class,
                    api.getUserManager().authenticate(config.getString("spigot.username"), config.getString("spigot.password"), config.getString("spigot.recovery-key")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
