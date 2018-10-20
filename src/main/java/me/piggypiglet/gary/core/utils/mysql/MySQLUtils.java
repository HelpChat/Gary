package me.piggypiglet.gary.core.utils.mysql;

import co.aikar.idb.DB;
import me.piggypiglet.gary.core.objects.tasks.Task;

import java.util.Arrays;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MySQLUtils {
    public static void add(String table, String[] keys, Object... values) {
        StringBuilder keysBuilder = new StringBuilder("(`id`");
        Arrays.stream(keys).forEach(k -> keysBuilder.append(", ").append(k));
        keysBuilder.append(")");

        StringBuilder valuesBuilder = new StringBuilder("('0'");
        for (Object value : values) valuesBuilder.append(", ").append("?");
        valuesBuilder.append(");");

        Task.async(r -> {
            try {
                DB.executeInsert("INSERT INTO ? ? VALUES " + valuesBuilder.toString(), table, keysBuilder.toString(), values);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
