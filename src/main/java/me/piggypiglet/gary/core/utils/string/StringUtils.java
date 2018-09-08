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

    public static boolean contains(String str, String str2) {
        str = str.toLowerCase();
        str2 = str2.toLowerCase();

        if (str.contains("/")) {
            String[] contain = str.split("/");
            String[] spaces = str.split(" ");

            if (spaces.length >= 2) {
                for (int i = 0; i < contain.length; ++i) {
                    contain[i] = contain[i] + " ";
                }

                return Arrays.stream(contain).anyMatch(str::endsWith);
            }

            return Arrays.stream(contain).anyMatch(str::contains);
        }

        return str.contains(str2);
    }
}
