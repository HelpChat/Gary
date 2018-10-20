package me.piggypiglet.gary.core.storage.mysql;

import co.aikar.idb.DB;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.objects.tasks.Task;
import me.piggypiglet.gary.core.storage.json.FileConfiguration;
import me.piggypiglet.gary.core.storage.json.GFile;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MySQLInitializer {
    @Inject private GFile gFile;

    public void connect() {
        Task.async(r -> {
            final FileConfiguration config = gFile.getFileConfiguration("config");
            final String[] tables = {"gary_faq", "gary_messages", "gary_stats"};
            final String[] schemas = ((String) gFile.getItemMaps().get("schema").get("file-content")).split("-");

            DatabaseOptions options = DatabaseOptions.builder().mysql(
                    config.getString("mysql-username"),
                    config.getString("mysql-password"),
                    config.getString("mysql-db"),
                    config.getString("mysql-host")
            ).build();

            DB.setGlobalDatabase(PooledDatabaseOptions.builder().options(options).createHikariDatabase());
            AtomicInteger i = new AtomicInteger(0);

            Arrays.stream(tables).forEach(t -> {
                try {
                    int i_ = i.getAndIncrement();

                    if (DB.getFirstRow("SHOW TABLES LIKE '" + t + "'") == null) {
                        DB.executeInsert(schemas[i_]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            LoggerFactory.getLogger("MySQL").info("Database successfully initialized.");
        }, "MySQL");
    }
}
