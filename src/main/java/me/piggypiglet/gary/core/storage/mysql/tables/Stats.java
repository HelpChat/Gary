package me.piggypiglet.gary.core.storage.mysql.tables;

import co.aikar.idb.DB;
import me.piggypiglet.gary.core.utils.message.StringUtils;
import net.dv8tion.jda.core.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Stats {
    private Logger logger;

    public Stats() {
        logger = LoggerFactory.getLogger("Stats");
    }

    public void add(String column, User user) {
        long discord_id = user.getIdLong();

        if (!StringUtils.equalsIgnoreCase(column, "win/o/bro")) return;

        try {
            int newValue = (int) DB.getFirstColumnAsync("SELECT " + column + " FROM `gary_stats` WHERE `discord_id`=?", discord_id).get() + 1;
            DB.executeUpdate("UPDATE `gary_stats` SET " + column + "=? WHERE `discord_id`=?", newValue, discord_id);
            logger.info("Added " + column + " to " + user.getName() + "#" + user.getDiscriminator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
