package me.piggypiglet.gary.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class StringUtils {
    public static boolean startsWith(String str, String element) {
        return startsWith(str, Collections.singletonList(element));
    }

    public static boolean startsWith(String str, List<String> elements) {
        return lowercaseParallelStream(elements).anyMatch(str.toLowerCase()::startsWith);
    }

    private static Stream<String> lowercaseParallelStream(List<String> list) {
        return list.stream().map(String::toLowerCase).parallel();
    }
}
