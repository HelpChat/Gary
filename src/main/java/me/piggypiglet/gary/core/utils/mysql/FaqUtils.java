package me.piggypiglet.gary.core.utils.mysql;

import me.piggypiglet.gary.core.objects.faq.Faq;

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class FaqUtils {
    public static boolean add(String key, String value, long userID) {
        return MySQLUtils.create("gary_faq", new String[] {"key", "value", "author"}, key, value, userID);
    }

    public static boolean edit(String key, String value) {
        return MySQLUtils.set("gary_faq", new AbstractMap.SimpleEntry<>("key", key), new AbstractMap.SimpleEntry<>(new String[]{"value"}, new String[]{value}));
    }

    public static String get(String key) {
        try {
            return MySQLUtils.getRow("gary_faq", "key", key).getString("value");
        } catch (Exception ignored) {}

        return "null";
    }

    public static boolean remove(String key) {
        return MySQLUtils.remove("gary_faq", "key", key);
    }

    public static List<Faq> getFaqs() {
        return MySQLUtils.getRows("gary_faq").stream().map(r -> new Faq(r.getString("key"), r.getString("value"), r.getLong("author"))).collect(Collectors.toList());
    }
}