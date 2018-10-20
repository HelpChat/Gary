package me.piggypiglet.gary.core.utils.mysql;

import co.aikar.idb.DB;
import me.piggypiglet.gary.core.objects.tasks.Task;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MySQLUtils {
    public static void add(String table, String[] keys, Object... values) {
        StringBuilder keysBuilder = new StringBuilder("(`id`");
        Arrays.stream(keys).forEach(k -> keysBuilder.append(", `").append(k).append("`"));
        keysBuilder.append(")");

        StringBuilder valuesBuilder = new StringBuilder("('0'");
        for (Object ignored : values) valuesBuilder.append(", ").append("%s");
        valuesBuilder.append(");");

        Task.async(r -> {
            try {
                DB.executeInsert(mysqlFormat("INSERT INTO " + table + " " + keysBuilder.toString() + " VALUES " + valuesBuilder.toString(), values));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static String mysqlFormat(String str, Object... params) {
        final AtomicReference<String> string = new AtomicReference<>(str);

        Arrays.stream(params).forEach(param -> {
            switch (param.getClass().getSimpleName()) {
                case "String":
                    string.set(string.get().replaceFirst("%s", "'" + param + "'"));
                    break;

                case "Long":
                    string.set(string.get().replaceFirst("%s", Long.toString((Long) param)));
                    break;
            }
        });

        return string.get();
    }
}
