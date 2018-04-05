package me.piggypiglet.gary.core.storage;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.objects.GFile;

import java.sql.SQLException;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public class MySQLSetup {
    @Inject private GFile config;

    public void connect() {
        DatabaseOptions options = DatabaseOptions.builder().mysql(
                config.getItem("config", "mysql-username"),
                config.getItem("config", "mysql-password"),
                config.getItem("config", "mysql-db"),
                config.getItem("config", "mysql-ip") + ":" + config.getItem("config", "mysql-port")
        ).build();
        // TODO: Fix this
        Database db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(db);
        String id = "id";
        try {
            System.out.println(db.getFirstRow("SELECT `?`, `discord_id`, `username`, `discriminator` FROM `admin_gary`.`gary_users` WHERE `id`=1;", id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
