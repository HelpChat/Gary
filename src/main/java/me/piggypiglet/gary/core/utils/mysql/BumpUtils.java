package me.piggypiglet.gary.core.utils.mysql;

import java.util.AbstractMap;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class BumpUtils {
    public static boolean create(long userId, long channelId, String message) {
        return MySQLUtils.create("gary_bumps", new String[]{"user_id", "channel_id", "message"}, userId, channelId, message);
    }

    public static boolean contains(long userId, long channelId, String message) {
        if (MySQLUtils.exists("gary_bumps", new String[]{"user_id", "channel_id"}, new Object[]{userId, channelId})) {
            return MySQLUtils.fuzzyWuzzy(
                    "gary_bumps",
                    new AbstractMap.SimpleEntry<>(new String[]{"user_id", "channel_id"}, new Object[]{userId, channelId}),
                    new AbstractMap.SimpleEntry<>("message", message),
                    80);
        }

        return false;
    }

    public static void clear() {
        MySQLUtils.purge("gary_bumps");
    }
}
