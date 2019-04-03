package me.piggypiglet.gary.core.utils.mysql;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class BumpUtils {
    public static boolean create(long userId, String message) {
        return MySQLUtils.create("gary_bumps", new String[]{"user_id", "message"}, userId, message);
    }

    public static boolean contains(long userId, String message) {
//        List<DbRow> rows =

        return MySQLUtils.exists("gary_bumps", new String[]{"user_id", "message"}, new Object[]{userId, message});
    }
}
