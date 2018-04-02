package me.piggypiglet.gary.core.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.piggypiglet.gary.core.objects.GFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public class MySQLSetup {
    @Inject private HikariConfig hikariConfig;
    @Inject private GFile config;
    private HikariDataSource ds;

    public void connect() {
        String url = config.getItem("config", "mysql-ip") +
                ":" +
                config.getItem("config", "mysql-port") +
                "/" +
                config.getItem("config", "mysql-db");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + url + "?useSSL=false");
        hikariConfig.setUsername(config.getItem("config", "mysql-username"));
        hikariConfig.setPassword(config.getItem("config", "mysql-password"));

        ds = new HikariDataSource(hikariConfig);
    }

    public void setup() {
        try {
            Connection con = ds.getConnection();
            Statement s = con.createStatement();
            boolean bool = s.execute(Arrays.toString(Files.lines(Paths.get("./schema/GaryBot.sql")).toArray())
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "\n"));
            System.out.println(bool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
