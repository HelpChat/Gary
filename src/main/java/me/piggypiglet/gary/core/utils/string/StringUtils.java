package me.piggypiglet.gary.core.utils.string;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class StringUtils {
    public static boolean startsWith(String str, String contain) {
        return Arrays.stream(contain.toLowerCase().split("/")).parallel().anyMatch(str.toLowerCase()::startsWith);
    }

    public static boolean contains(String str, String contain) {
        return contains(str, Arrays.asList(contain.split("/")));
    }

    public static boolean contains(String str, List<String> items) {
        return lowercaseParallelStream(items).anyMatch(str.toLowerCase()::contains);
    }

    public static boolean containsAll(String str, List<String> items) {
        return lowercaseParallelStream(items).allMatch(str.toLowerCase()::contains);
    }

    private static Stream<String> lowercaseParallelStream(List<String> list) {
        return list.stream().map(String::toLowerCase).parallel();
    }

}
