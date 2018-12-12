package me.piggypiglet.gary.core.utils.mysql;

import me.piggypiglet.gary.core.objects.tasks.Task;
import me.piggypiglet.gary.core.utils.misc.PunishmentUtils;

import java.util.AbstractMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class WarningsUtils {
    public static boolean add(long userId, int... amount) {
        final AtomicInteger strikes = new AtomicInteger(amount.length >= 1 ? amount[0] : 1);
        boolean success;

        if (MySQLUtils.exists("gary_warnings", "user_id", userId)) {
            strikes.set(strikes.get() + Objects.requireNonNull(MySQLUtils.getRow("gary_warnings", "user_id", userId)).getInt("strikes"));

            success = MySQLUtils.set(
                   "gary_warnings",
                   new AbstractMap.SimpleEntry<>("user_id", userId),
                   new AbstractMap.SimpleEntry<>(new String[]{"strikes"}, new Object[]{strikes.get()})
            );
        } else {
            success = MySQLUtils.create(
                    "gary_warnings",
                    new String[]{"user_id", "strikes"},
                    userId, strikes.get());
        }

        Task.async(r -> PunishmentUtils.update(userId, strikes.get()));

        return success;
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