package me.piggypiglet.gary.core.storage;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.piggypiglet.gary.core.objects.GFile;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class MySQLSetup {
    @Inject private HikariConfig hikariConfig;
    @Inject private GFile config;

    public void connect() {
        String url = config.getItem("config", "mysql-ip") +
                ":" +
                config.getItem("config", "mysql-port") +
                "/" +
                config.getItem("config", "mysql-db");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + url + "?useSSL=false");
        hikariConfig.setUsername(config.getItem("config", "mysql-username"));
        hikariConfig.setPassword(config.getItem("config", "mysql-password"));

        HikariDataSource ds = new HikariDataSource(hikariConfig);
    }
}
