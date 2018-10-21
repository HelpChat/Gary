package me.piggypiglet.gary.core.utils.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class StringUtils {
    public static boolean startsWith(String str, String contain) {
        return lowercaseParallelStream(Arrays.asList(contain.split("/"))).anyMatch(str.toLowerCase()::startsWith);
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

    /**
     * Splits a message using a specified pattern.<br/>
     * Pattern format is <strong>word-word|word-word</strong>. Program will get any content between the specified words, and split the content in there by spaces. It'll leave double quote words intact. Use | to specify multiple argument sections.
     * <br/>EG: message: !gary please help me with my homework, pattern: please-with|my-, would return: {"help", "me", "homework"}
     * @param message The message to apply the pattern on
     * @param pattern The pattern to split the message with
     * @return Returns an array of the splitted message using the pattern.
     */
    public static String[] commandSplit(String message, String pattern) {
        List<String> baseArgs = new ArrayList<>();
        List<String> args = new ArrayList<>();
        String[] sections = pattern.split("\\|");

        for (int i = 0; i < sections.length; ++i) {
            String[] argAreas = sections[i].split("-");

            if (argAreas.length >= 2) {
                baseArgs.add(org.apache.commons.lang3.StringUtils.substringBetween(message, argAreas[0], argAreas[1]));
            } else {
                String str = "╚";

                if (sections[i].startsWith("-")) {
                    if (i == 0) {
                        message = str + message;
                    } else {
                        str = sections[i - 1].split("-")[0];
                    }
                } else if (sections[i].endsWith("-")) {
                    if (sections.length > i + 1) {
                        str = sections[i + 1].split("-")[0];
                    } else {
                        message = message + str;
                    }
                }

                System.out.println(message);

                baseArgs.add(org.apache.commons.lang3.StringUtils.substringBetween(message, argAreas[0], str));
            }
        }

        baseArgs.forEach(ba -> args.addAll(Arrays.asList(ba.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)"))));
        String[] result = args.stream().filter(x -> !org.apache.commons.lang3.StringUtils.isBlank(x)).toArray(String[]::new);

        return result.length == 0 ? new String[]{} : result[0].isEmpty() ? new String[]{} : result;
    }
}
