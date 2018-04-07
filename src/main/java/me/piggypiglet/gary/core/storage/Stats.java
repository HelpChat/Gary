package me.piggypiglet.gary.core.storage;

import co.aikar.idb.DB;
import net.dv8tion.jda.core.entities.User;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class Stats {
    public void addWin(User user) {
        long discord_id = user.getIdLong();

        try {
            int wins = (int) DB.getFirstColumnAsync("SELECT `wins` FROM `gary_cr_stats` WHERE `discord_id`=?", discord_id).get();
            DB.executeUpdate("UPDATE `gary_cr_stats` SET `wins`=? WHERE `discord_id`=?", wins + 1, discord_id);
            System.out.println("Added a win to " + user.getName() + "#" + user.getDiscriminator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
