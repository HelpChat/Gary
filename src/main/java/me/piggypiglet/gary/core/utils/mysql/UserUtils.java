package me.piggypiglet.gary.core.utils.mysql;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.storage.tables.Users;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class UserUtils {
    @Inject private Users users;

    public String checkUsers(Guild guild) {
        List<Long> members = new ArrayList<>();
        List<Long> dbMembers = new ArrayList<>();

        guild.getMembers().forEach(member -> members.add(member.getUser().getIdLong()));

        try {
            List<DbRow> rows = DB.getResultsAsync("SELECT * FROM `gary_users`").get();
            rows.forEach(id -> dbMembers.add(id.getLong("discord_id")));

            if (!dbMembers.containsAll(members)) {
                return "The database is not synced.";
            } else {
                return "The database is synced.";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String syncUsers(Guild guild, JDA jda) {
        List<Long> members = new ArrayList<>();
        List<Long> database = new ArrayList<>();

        guild.getMembers().forEach(member -> members.add(member.getUser().getIdLong()));

        try {
            DB.getResultsAsync("SELECT * FROM `gary_users`").get().forEach(member -> database.add(member.getLong("discord_id")));

            if (database.size() > members.size()) {
                members.forEach(database::remove);

                database.forEach(users::delUser);
                database.clear();
            }

            if (database.isEmpty()) {
                DB.getResultsAsync("SELECT * FROM `gary_users`").get().forEach(member -> database.add(member.getLong("discord_id")));
            }

            database.forEach(members::remove);

            members.forEach(member -> users.addUser(jda.getUserById(member)));

            String users = members.size() >= 2 || members.size() == 0 ? members.size() + " users have" : "1 user has";
            return users + " been synced to the database.";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
