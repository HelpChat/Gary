package me.piggypiglet.gary.core.storage.file;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.utils.string.StringUtils;

import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Lang {
    @Inject private static GFile gFile;

    public static String getString(String path, Object... variables) {
        return String.format(gFile.getFileConfiguration("lang").getString(path), variables);
    }

    public static List<String> getStringList(String path, Object... variables) {
        return StringUtils.formatList(gFile.getFileConfiguration("lang").getStringList(path), variables);
    }
}
