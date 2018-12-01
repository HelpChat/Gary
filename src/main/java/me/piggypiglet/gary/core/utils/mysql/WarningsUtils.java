package me.piggypiglet.gary.core.utils.mysql;

import java.util.AbstractMap;
import java.util.Objects;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class WarningsUtils {
    public static boolean add(long userId, int... amount) {
        int strikes = amount.length >= 1 ? amount[0] : 1;

        if (MySQLUtils.exists("gary_warnings", "user_id", userId)) {
            int currentStrikes = strikes + Objects.requireNonNull(MySQLUtils.getRow("gary_warnings", "user_id", userId)).getInt("strikes");

            return MySQLUtils.set(
                   "gary_warnings",
                   new AbstractMap.SimpleEntry<>("user_id", userId),
                   new AbstractMap.SimpleEntry<>(new String[]{"strikes"}, new Object[]{currentStrikes})
            );
        }

        return MySQLUtils.create(
                "gary_warnings",
                new String[]{"user_id", "strikes"},
                userId, strikes);
    }

    public static boolean remove(long userId, int... amount) {
        int strikes = amount.length >= 1 ? amount[0] : 1;

        if (MySQLUtils.exists("gary_warnings", "user_id", userId)) {
            int currentStrikes = Objects.requireNonNull(MySQLUtils.getRow("gary_warnings", "user_id", userId)).getInt("strikes") - strikes;

            if (currentStrikes <= 0) {
                return MySQLUtils.remove("gary_warnings", "user_id", userId);
            }

            return MySQLUtils.set(
                    "gary_warnings",
                    new AbstractMap.SimpleEntry<>("user_id", userId),
                    new AbstractMap.SimpleEntry<>(new String[]{"strikes"}, new Object[]{currentStrikes})
            );
        }

        return false;
    }
}