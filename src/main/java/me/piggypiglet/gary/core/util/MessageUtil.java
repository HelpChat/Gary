package me.piggypiglet.gary.core.util;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public class MessageUtil {
    public String format(MessageReceivedEvent e, String str) {
        if (str.contains("%time%")) {
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+08:00"));
            String am = cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            str = str.replace("%time%", cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + " " + am);
        }
        return str
                .replace("%n%", "\n")
                .replace("%name%", e.getAuthor().getName())
                .replace("%id%", e.getAuthor().getId());
    }
    public boolean contains(String msg, List<String> contain) {
        return contain.parallelStream().allMatch(msg.toLowerCase()::contains);
    }
    public boolean startsWith(String msg, String str) {
        if (str.contains("/")) {
            String[] contain = str.split("/");
            for (String string : contain) {
                if (msg.toLowerCase().startsWith(string)) {
                    return true;
                }
            }
        }
        return msg.toLowerCase().startsWith(str);
    }
}
