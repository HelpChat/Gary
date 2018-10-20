package me.piggypiglet.gary.core.handlers;

import co.aikar.idb.DB;
import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.tasks.Task;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ShutdownHandler extends Thread {
    @Inject private GaryBot garyBot;

    /**
     * Closes MySQL connection, disconnects JDA from Discord, shuts down all threads in the thread pool, program then exits.
     */
    @Override
    public void run() {
        DB.close();
        garyBot.getJda().shutdownNow();
        Task.shutdown();
    }
}
