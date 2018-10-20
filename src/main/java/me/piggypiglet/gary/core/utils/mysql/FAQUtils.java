package me.piggypiglet.gary.core.utils.mysql;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class FAQUtils {
    public static void addFaq(String key, String value, long userID) {
        MySQLUtils.add("gary_faq", new String[] {"identifier", "message", "user_id"}, key, value, userID);
    }
}
