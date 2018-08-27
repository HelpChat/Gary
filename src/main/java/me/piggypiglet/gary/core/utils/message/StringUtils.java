package me.piggypiglet.gary.core.utils.message;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.utils.misc.TimeUtils;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class StringUtils {
    @Inject private TimeUtils tutil;

    public String format(GuildMessageReceivedEvent e, String str) {
        User author = e.getAuthor();
        return str
                .replace("%n%", "\n")
                .replace("%name%", author.getName())
                .replace("%id%", author.getId())
                .replace("%user%", author.getAsMention())
                .replace("%time%", tutil.getTime());
    }

    public static boolean contains(String msg, List<String> contain) {
        return contain.parallelStream().allMatch(msg.toLowerCase()::contains);
    }

    public static String bigLetters(String word) {
        List<String> letters = Arrays.asList(word.split(""));
        List<String> newLetters = new ArrayList<>();
        letters.forEach(letter -> newLetters.add(letter.equals("_") ? "**_** " : letter.equals(".") ? "**.** " : NumberUtils.isCreatable(letter) ? convertNumToEmoji(Integer.valueOf(letter)) : ":regional_indicator_" + letter +  ": "));
        return newLetters.toString()
                .replace("]", "")
                .replace("[", "")
                .replace(", ", "")
                .toLowerCase();
    }

    private static String convertNumToEmoji(int num) {
        switch (num) {
            case 1:
                return ":one: ";
            case 2:
                return ":two: ";
            case 3:
                return ":three: ";
            case 4:
                return ":four: ";
            case 5:
                return ":five: ";
            case 6:
                return ":six: ";
            case 7:
                return ":seven: ";
            case 8:
                return ":eight: ";
            case 9:
                return ":nine: ";
        }
        return null;
    }

    public static boolean startsWith(String str, String check) {
        str = str.toLowerCase();
        check = check.toLowerCase();

        if (check.contains("/")) {
            String[] contain = check.split("/");
            return Arrays.stream(contain).anyMatch(str::startsWith);
        }
        return str.startsWith(check);
    }

    public static boolean equalsIgnoreCase(String str, String check) {
        if (check.contains("/")) {
            String[] equals = check.split("/");
            return Arrays.stream(equals).anyMatch(str::equalsIgnoreCase);
        }

        return str.equalsIgnoreCase(check);
    }

    public static boolean hasWord(String str, String word) {
        str = str.trim();

        return str.contains(" " + word + " ") || str.startsWith(word + " ") || str.endsWith(" " + word) || str.equalsIgnoreCase(word);
    }

    public static String getFirst(String msg) {
        msg = msg.trim();

        if (msg.contains("/")) {
            String[] split = msg.split("/");
            return split.length >= 1 ? split[0].trim() : msg;
        }

        return msg.trim();
    }

    public static boolean contains(String str, String contain) {
        str = str.toLowerCase();
        contain = contain.toLowerCase();

        if (contain.contains("/")) {
            String[] split = contain.split("/");
            return Arrays.stream(split).allMatch(str::contains);
        }

        return str.contains(contain);
    }

    public static String arrayToString(String[] array, String delimiter) {
        return Arrays.stream(array).collect(Collectors.joining(delimiter));
    }

    public static String listToString(List<String> list, String delimiter) {
        return arrayToString(list.toArray(new String[]{}), delimiter);
    }
}
