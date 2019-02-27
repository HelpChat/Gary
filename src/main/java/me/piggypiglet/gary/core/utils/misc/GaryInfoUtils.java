package me.piggypiglet.gary.core.utils.misc;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.handlers.chat.CommandHandler;
import me.piggypiglet.gary.core.handlers.misc.LoggingHandler;
import me.piggypiglet.gary.core.storage.file.GFile;

import java.lang.management.ManagementFactory;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class GaryInfoUtils {
    @Inject private static GFile gFile;
    @Inject private static CommandHandler commandHandler;
    @Inject private static LoggingHandler loggingHandler;

    public static long getUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    public static String getVersion() {
        return gFile.getFileConfiguration("embed").getString("version");
    }

    public static int getLoadedCommands() {
        return commandHandler.getCommands().size();
    }

    public static int getLoadedLoggers() {
        return loggingHandler.getLoggers().size();
    }
}
