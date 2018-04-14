package me.piggypiglet.gary.core.storage;

import co.aikar.idb.DB;
import net.dv8tion.jda.core.entities.User;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class Users {
    public void addUser(User user) {
        long discord_id = user.getIdLong();
        String username = user.getName();
        String discriminator = user.getDiscriminator();

        try {
            DB.executeInsert("INSERT INTO `gary_users` (`id`, `discord_id`, `username`, `discriminator`) VALUES ('0', ?, ?, ?);", discord_id, username, discriminator);
            DB.executeInsert("INSERT INTO `gary_stats` (`id`, `discord_id`, `win`, `o`, `bro`) VALUES ('0', ?, '0', '0', '0');", discord_id);
            System.out.println(username + "#" + discriminator + " has been added to the database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delUser(long discord_id) {
        try {
            DB.executeUpdate("DELETE FROM `gary_users` WHERE `discord_id`=?;", discord_id);
            DB.executeUpdate("DELETE FROM `gary_stats` WHERE `discord_id`=?;", discord_id);
            System.out.println(discord_id + " has been removed from the database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}