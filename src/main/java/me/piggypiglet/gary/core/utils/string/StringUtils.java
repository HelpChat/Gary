package me.piggypiglet.gary.core.utils.string;

import java.util.Arrays;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class StringUtils {
    public static boolean startsWith(String msg, String str) {
        msg = msg.toLowerCase();
        str = str.toLowerCase();

        if (str.contains("/")) {
            String[] contain = str.split("/");
            return Arrays.stream(contain).anyMatch(msg::startsWith);
        }

        return msg.startsWith(str);
    }

    public static boolean contains(String str, String contain) {
        str = str.toLowerCase();
        contain = contain.toLowerCase();

        if (contain.contains("/")) {
            String[] split = contain.split("/");
            return Arrays.stream(split).anyMatch(str::contains);
        }

        return str.contains(contain);
    }
}
