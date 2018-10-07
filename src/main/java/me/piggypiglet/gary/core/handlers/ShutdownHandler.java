package me.piggypiglet.gary.core.handlers;

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
     * Disconnects JDA from Discord, then shuts down all threads in the thread pool. Program then exits.
     */
    @Override
    public void run() {
        garyBot.getJda().shutdownNow();
        Task.shutdown();
    }
}
