package me.piggypiglet.gary.core.utils.misc;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class TimeUtils {
    public String getTime() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+08:00"));
        String time = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);

        return time + " " + (calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");
    }
}
