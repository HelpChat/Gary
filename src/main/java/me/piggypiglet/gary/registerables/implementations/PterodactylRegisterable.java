package me.piggypiglet.gary.registerables.implementations;

import com.google.inject.Inject;
import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.PteroUserAPI;
import me.piggypiglet.gary.file.framework.FileConfiguration;
import me.piggypiglet.gary.guice.annotations.Config;
import me.piggypiglet.gary.registerables.Registerable;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class PterodactylRegisterable extends Registerable {
    @Inject @Config private FileConfiguration config;

    @Override
    protected void execute() {
        final String url = config.getString("pterodactyl.url");

        addBinding(new PteroAdminAPI(url, config.getString("pterodactyl.admin-token")));
        addBinding(new PteroUserAPI(url, config.getString("pterodactyl.user-token")));
    }
}
