package me.piggypiglet.gary.core.utils.string;

import java.util.Arrays;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class StringUtils {
    public static boolean startsWith(String str, String contain) {
        return Arrays.stream(contain.toLowerCase().split("/")).parallel().anyMatch(str.toLowerCase()::startsWith);
    }

    public static boolean contains(String str, String contain) {
        return Arrays.stream(contain.toLowerCase().split("/")).parallel().anyMatch(str.toLowerCase()::contains);
    }
}
