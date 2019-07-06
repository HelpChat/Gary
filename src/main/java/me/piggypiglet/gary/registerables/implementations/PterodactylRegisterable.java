package me.piggypiglet.gary.registerables.implementations;

import com.google.inject.Inject;
import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.misc.Logger;
import me.piggypiglet.gary.file.framework.FileConfiguration;
import me.piggypiglet.gary.guice.annotations.Config;
import me.piggypiglet.gary.registerables.Registerable;
import org.slf4j.LoggerFactory;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class PterodactylRegisterable extends Registerable {
    @Inject @Config private FileConfiguration config;

    @Override
    protected void execute() {
        final String url = config.getString("pterodactyl.url");

        Logger logger = new Logger() {
            private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("Ptero4J");

            @Override
            public void info(String s) {
                LOGGER.info(s);
            }

            @Override
            public void warning(String s) {
                LOGGER.warn(s);
            }

            @Override
            public void error(String s) {
                LOGGER.error(s);
            }
        };

        addBinding(new PteroAdminAPI(url, config.getString("pterodactyl.admin-token"), logger));
        addBinding(new PteroUserAPI(url, config.getString("pterodactyl.user-token"), logger));
    }
}
