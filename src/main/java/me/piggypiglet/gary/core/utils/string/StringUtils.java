package me.piggypiglet.gary.core.utils.string;

import me.piggypiglet.gary.core.storage.file.Lang;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class StringUtils {
    public static boolean startsWith(String str, String contain) {
        return startsWith(str, Arrays.asList(contain.split("/")));
    }

    public static boolean startsWith(String str, List<String> elements) {
        return lowercaseParallelStream(elements).anyMatch(str.toLowerCase()::startsWith);
    }

    public static boolean endsWith(String str, List<String> elements) {
        return lowercaseParallelStream(elements).anyMatch(str.toLowerCase()::endsWith);
    }

    public static boolean contains(String str, String contain) {
        return contains(str, Arrays.asList(contain.split("/")));
    }

    public static boolean contains(String str, List<String> elements) {
        return lowercaseParallelStream(elements).anyMatch(str.toLowerCase()::contains);
    }

    public static boolean containsAll(String str, List<String> elements) {
        return lowercaseParallelStream(elements).allMatch(str.toLowerCase()::contains);
    }

    private static Stream<String> lowercaseParallelStream(List<String> list) {
        return list.stream().map(String::toLowerCase).parallel();
    }

    public static String replaceLast(String str, String regex, String replacement) {
        return str.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    public static boolean equalsIgnoreCase(String str, String... elements) {
        return lowercaseParallelStream(Arrays.asList(elements)).anyMatch(str::equalsIgnoreCase);
    }

    public static List<String> replaceAll(List<String> list, String key, String regex) {
        return list.stream().map(i -> i.replace(key, regex)).collect(Collectors.toList());
    }

    public static List<String> formatList(List<String> list, Object... variables) {
        return list.stream().map(s -> String.format(s, variables)).collect(Collectors.toList());
    }

    public static List<String> alternateFormat(List<String> list, Object... variables) {
        return Arrays.asList(String.format(String.join("\n", list), variables).split("/"));
    }

    public static String[] commandSplit(String message, List<String> replacements) {
        final AtomicReference<String> msg = new AtomicReference<>(message);
        replacements.addAll(Lang.getStringList("commands.prefix"));
        replacements.forEach(s -> msg.set(msg.get().replaceFirst(s, "")));
        String[] args = msg.get().trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        return args.length == 0 ? new String[] {} : args;
    }
}
