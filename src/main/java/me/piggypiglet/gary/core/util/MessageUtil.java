package me.piggypiglet.gary.core.util;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class MessageUtil {

    public String format(MessageReceivedEvent e, String str) {
        if (str.contains("%time%")) {
            GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+08:00"));

            String period = calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            str = str.replace("%time%", calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " + period);
        }

        User author = e.getAuthor();
        return str.replace("%n%", "\n").replace("%name%", author.getName()).replace("%id%", author.getId());
    }

    boolean contains(String msg, List<String> contain) {
        return contain.parallelStream().allMatch(msg.toLowerCase()::contains);
    }

    public boolean startsWith(String msg, String str) {
        if (str.contains("/")) {
            String[] contain = str.split("/");
            return Arrays.stream(contain).anyMatch(msg::startsWith);
        }
        return msg.toLowerCase().startsWith(str);
    }

    public String arrayToString(String[] array) {
        return Arrays.toString(array).replace("[", "").replace("]", "").replace(", ", " ");
    }

}
