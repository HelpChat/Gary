package me.piggypiglet.gary.core.tasks;

import com.google.inject.Inject;
import net.dv8tion.jda.core.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RunTasks {
    @Inject private WordChanger wc;

    private Logger logger;

    public RunTasks() {
        logger = LoggerFactory.getLogger("Tasks");
    }

    public void setup(JDA jda) {
        wc.setup(jda);
    }

    public void runTasks() {
        Timer timer = new Timer();

        long timeCR = TimeUnit.MINUTES.toMillis(7);
        timer.schedule(wc, TimeUnit.SECONDS.toMillis(1), timeCR);
        logger.info("Task - ChatReaction started");
    }
}
