package me.piggypiglet.gary.core.utils.channel;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.utils.misc.TimeUtils;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class MessageUtils {
    @Inject private TimeUtils tutil;

    public String format(MessageReceivedEvent e, String str) {
        User author = e.getAuthor();
        return str
                .replace("%n%", "\n")
                .replace("%name%", author.getName())
                .replace("%id%", author.getId())
                .replace("%user%", author.getAsMention())
                .replace("%time%", tutil.getTime());
    }

    boolean contains(String msg, List<String> contain) {
        return contain.parallelStream().allMatch(msg.toLowerCase()::contains);
    }

    public String bigLetters(String word) {
        List<String> letters = Arrays.asList(word.split(""));
        List<String> newLetters = new ArrayList<>();
        letters.forEach(letter -> newLetters.add(letter.equals("_") ? "**_** " : letter.equals(".") ? "**.** " : NumberUtils.isCreatable(letter) ? convertNumToEmoji(Integer.valueOf(letter)) : ":regional_indicator_" + letter +  ": "));
        return newLetters.toString()
                .replace("]", "")
                .replace("[", "")
                .replace(", ", "")
                .toLowerCase();
    }

    private String convertNumToEmoji(int num) {
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

    public boolean startsWith(String msg, String str) {
        if (str.contains("/")) {
            String[] contain = str.toLowerCase().split("/");
            return Arrays.stream(contain).anyMatch(msg.toLowerCase()::startsWith);
        }
        return msg.toLowerCase().startsWith(str.toLowerCase());
    }

    public String arrayToString(String[] array) {
        return Arrays.toString(array).replace("[", "").replace("]", "").replace(", ", " ");
    }

}
