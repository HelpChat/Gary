package me.piggypiglet.gary.tasks;

import sh.okx.timeapi.TimeAPI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class Task {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(10);

    public static void async(final Consumer<GRunnable> task) {
        EXECUTOR.submit(new GRunnable() {
            @Override
            public void run() {
                task.accept(this);
            }
        });
    }

    public static void async(final Consumer<GRunnable> task, String time) {
        TimeAPI timeAPI = new TimeAPI(time);

        SCHEDULER.schedule(new GRunnable() {
            @Override
            public void run() {
                task.accept(this);
            }
        }, timeAPI.getMilliseconds(), TimeUnit.MILLISECONDS);
    }

    public static void shutdown() {
        EXECUTOR.shutdownNow();
        SCHEDULER.shutdownNow();
    }
}
