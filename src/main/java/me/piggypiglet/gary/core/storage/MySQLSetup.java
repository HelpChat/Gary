package me.piggypiglet.gary.core.storage;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.piggypiglet.gary.core.objects.Config;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class MySQLSetup {
    @Inject private HikariConfig hikariConfig;
    @Inject private Config config;
    private HikariDataSource ds;

    public MySQLSetup() {
        String url = config.getItem("mysql-ip") +
                ":" +
                config.getItem("mysql-port") +
                "/" +
                config.getItem("mysql-db");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + url);
        hikariConfig.setUsername(config.getItem("mysql-username"));
        hikariConfig.setPassword(config.getItem("mysql-password"));
    }

    public void connect() {
        ds = new HikariDataSource(hikariConfig);
    }
}
