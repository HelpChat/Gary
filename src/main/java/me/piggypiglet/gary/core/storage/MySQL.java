package me.piggypiglet.gary.core.storage;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.utils.channel.MessageUtils;
import net.dv8tion.jda.core.JDA;
import org.intellij.lang.annotations.Language;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public class MySQL {
    @Inject private GFile gFile;
    @Inject private MessageUtils messageUtils;
    @Inject private Users users;

    public void connect() {
        DatabaseOptions options = DatabaseOptions.builder().mysql(
                gFile.getItem("config", "mysql-username"),
                gFile.getItem("config", "mysql-password"),
                gFile.getItem("config", "mysql-db"),
                gFile.getItem("config", "mysql-ip") + ":" + gFile.getItem("config", "mysql-port")
        ).build();
        Database db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(db);
    }

    public void setup(JDA jda) {
        try {
            if (DB.getFirstRow("SHOW TABLES LIKE 'gary_users'") == null) {
                @Language("MySQL")
                String users_ = Arrays.toString(Files.lines(Paths.get(gFile.getFile("users").getPath())).toArray())
                        .replace("]", "")
                        .replace("[", "")
                        .replace(", ", "\n");
                String crstats = Arrays.toString(Files.lines(Paths.get(gFile.getFile("crstats").getPath())).toArray())
                        .replace("]", "")
                        .replace("[", "")
                        .replace(", ", "\n");
                Stream.of(
                        users_,
                        crstats
                ).forEach(this::sql);

                jda.getGuildById(Constants.HELP_CHAT).getMembers().forEach(member -> users.addUser(member.getUser()));
            }

            System.out.println("database successfully loaded.");

            System.out.println(DB.getFirstColumnAsync("SELECT `wins` FROM `gary_cr_stats` WHERE `id`=1").get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sql(@Language("MySQL") String sql) {
        try {
            DB.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
