package me.piggypiglet.gary.core.storage;

import co.aikar.idb.DB;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.entities.User;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class Stats {
    @Inject private MessageUtils messageUtils;

    public void add(String column, User user) {
        long discord_id = user.getIdLong();

        if (!messageUtils.equalsIgnoreCase(column, "win/o/bro")) return;

        try {
            int newValue = (int) DB.getFirstColumnAsync("SELECT " + column + " FROM `gary_stats` WHERE `discord_id`=?", discord_id).get() + 1;
            DB.executeUpdate("UPDATE `gary_stats` SET " + column + "=? WHERE `discord_id`=?", newValue, discord_id);
            System.out.println("Added " + column + " to " + user.getName() + "#" + user.getDiscriminator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
