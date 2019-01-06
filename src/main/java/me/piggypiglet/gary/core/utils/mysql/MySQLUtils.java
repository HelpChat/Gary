package me.piggypiglet.gary.core.utils.mysql;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import me.piggypiglet.gary.core.objects.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MySQLUtils {
    public static boolean create(String table, String[] keys, Object... values) {
        CompletableFuture<Boolean> success = new CompletableFuture<>();
        StringBuilder keysBuilder = new StringBuilder("(`id`");
        Arrays.stream(keys).forEach(k -> keysBuilder.append(", `").append(k).append("`"));
        keysBuilder.append(")");

        StringBuilder valuesBuilder = new StringBuilder("('0'");
        for (Object ignored : values) valuesBuilder.append(", ").append("%s");
        valuesBuilder.append(");");

        Task.async(r -> {
            try {
                DB.executeInsert(mysqlFormat("INSERT INTO " + table + " " + keysBuilder.toString() + " VALUES " + valuesBuilder.toString(), values));
                success.complete(true);
            } catch (Exception e) {
                success.complete(false);
                e.printStackTrace();
            }
        });

        //noinspection StatementWithEmptyBody
        while (!success.isDone());

        try {
            return success.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean set(String table, Map.Entry<String, Object> location, Map.Entry<String[], Object[]> replace) {
        boolean success = false;
        String[] replaceKeys = replace.getKey();
        Object[] replaceValues = replace.getValue();
        int replaceKeysLength = replaceKeys.length;

        if (replaceKeysLength != 0 && replaceKeysLength == replaceValues.length) {
            StringBuilder replacements = new StringBuilder();

            for (int i = 0; i < replaceKeysLength - 2; ++i) {
                replacements.append(replaceKeys[i]).append("=").append("%s");
            }

            replacements.append(replaceKeys[replaceKeysLength - 1]).append("=").append("%s");

            try {
                if (exists(table, location.getKey(), location.getValue())) {
                    DB.executeUpdateAsync("UPDATE `" + table + "` SET " + mysqlFormat(replacements.toString(), replaceValues) + " WHERE `" + location.getKey() + "`=?;", location.getValue());
                    success = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public static DbRow getRow(String table, String key, Object value) {
        try {
            return DB.getFirstRowAsync("SELECT * FROM `" + table + "` WHERE `" + key + "`=?;", value).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DbRow> getRows(String table) {
        try {
            return DB.getResultsAsync("SELECT * FROM `" + table + "`;").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static boolean remove(String table, String key, Object value) {
        boolean success = false;

        try {
            if (exists(table, key, value)) {
                DB.executeUpdateAsync("DELETE FROM `" + table + "` WHERE `" + key + "`=?;", value);
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    public static boolean exists(String table, String key, Object value) {
        boolean exists = false;

        try {
            exists = DB.getFirstRowAsync("SELECT * FROM `" + table + "` WHERE `" + key + "`=?;", value).get() != null;
        } catch (Exception ignored) {}

        return exists;
    }

    private static String mysqlFormat(String str, Object... params) {
        final AtomicReference<String> string = new AtomicReference<>(str);

        Arrays.stream(params).forEach(param -> {
            switch (param.getClass().getSimpleName()) {
                case "String":
                    string.set(string.get().replaceFirst("%s", "'" + param + "'"));
                    break;

                case "Integer":
                    string.set(string.get().replaceFirst("%s", Integer.toString((Integer) param)));
                    break;

                case "Long":
                    string.set(string.get().replaceFirst("%s", Long.toString((Long) param)));
                    break;
            }
        });

        return string.get();
    }
}