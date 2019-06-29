package me.piggypiglet.gary;

import com.google.inject.Inject;
import me.piggypiglet.gary.tasks.Task;
import net.dv8tion.jda.api.JDA;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class ShutdownHook extends Thread {
    @Inject private JDA jda;

    @Override
    public void run() {
        jda.shutdownNow();
        Task.shutdown();
    }
}
