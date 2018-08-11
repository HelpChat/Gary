package me.piggypiglet.gary.core.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class RunTasks {
    @Inject private WordChanger wc;

    private Logger logger;
    private Map<TimerTask, Timer> taskTimerMap;

    public RunTasks() {
        logger = LoggerFactory.getLogger("Tasks");
        taskTimerMap = new HashMap<>();
    }

    public void runTasks() {
        Timer timer = new Timer();

        long timeCR = TimeUnit.MINUTES.toMillis(7);
        timer.schedule(wc, TimeUnit.SECONDS.toMillis(5), timeCR);
        logger.info("Task - ChatReaction started");
    }

    public void newTask(TimerTask task, long interval, boolean startImmediately) {
        Timer timer = new Timer();
        long startTime = startImmediately ? TimeUnit.SECONDS.toMillis(1) : interval;

        timer.schedule(task, startTime, interval);
        taskTimerMap.put(task, timer);
    }

    public void killTask(TimerTask task) {
        task.cancel();
        taskTimerMap.get(task).cancel();
        taskTimerMap.remove(task);
    }
}
