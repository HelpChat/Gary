package me.piggypiglet.gary.core.utils.mysql;

import co.aikar.idb.DbRow;
import me.piggypiglet.gary.core.objects.enums.ChatSettings;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class SettingsUtils {
    public static void add(long userID, ChatSettings setting) {
        int[] v = new int[]{0,0,0,0,0};

        switch (setting) {
            case GLOBAL_ANNOUNCEMENTS: v[0] = 1; break;
            case PLUGIN_UPDATES: v[1] = 1; break;
            case PAPI_GIT: v[2] = 1; break;
            case CLIP_PING: v[3] = 1; break;
            case CHAT_REACTION: v[4] = 1; break;
        }

        if (!MySQLUtils.exists("gary_settings", new String[]{"user_id"}, new Object[]{userID})) {
            MySQLUtils.create("gary_settings", new String[] {"user_id", "global_announcements", "plugin_updates", "papi_git", "clip_ping", "chat_reaction"}, userID, v[0], v[1], v[2], v[3], v[4]);
        }
    }

    public static void set(ChatSettings setting, long userId, int value) {
        if (value == 0) {
            DbRow row = MySQLUtils.getRow("gary_settings", new String[] {"user_id"}, new Object[] {userId});
            List<String> columns = new ArrayList<>();
            Stream.of("global_announcements", "plugin_updates", "papi_git", "clip_ping", "chat_reaction").forEach(columns::add);
            columns.remove(setting.toString().toLowerCase());

            assert row != null;

            if (columns.stream().allMatch(s -> row.getInt(s) == 0)) {
                MySQLUtils.remove("gary_settings", new String[]{"user_id"}, new Object[]{userId});
                return;
            }
        }

        MySQLUtils.set("gary_settings", new AbstractMap.SimpleEntry<>("user_id", userId), new AbstractMap.SimpleEntry<>(new String[]{setting.toString().toLowerCase()}, new Object[]{value}));
    }
}
