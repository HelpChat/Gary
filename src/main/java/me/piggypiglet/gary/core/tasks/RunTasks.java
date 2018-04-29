package me.piggypiglet.gary.core.tasks;

import com.google.inject.Inject;
import net.dv8tion.jda.core.JDA;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RunTasks {
    @Inject private WordChanger wc;

    public void setup(JDA jda) {
        wc.setup(jda);
    }

    public void runTasks() {
        Timer timer = new Timer();

        System.out.println("Task - ChatReaction started");
        long timeCR = TimeUnit.MINUTES.toMillis(7);
        timer.schedule(wc, TimeUnit.SECONDS.toMillis(1), timeCR);
    }
}
