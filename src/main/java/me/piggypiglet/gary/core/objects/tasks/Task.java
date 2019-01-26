package me.piggypiglet.gary.core.objects.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.GaryBot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class Task {
    @Inject private static GaryBot main;

    private static ExecutorService executor;
    private static ScheduledExecutorService scheduledExecutor;

    private Task() {
        executor = Executors.newFixedThreadPool(10);
        scheduledExecutor = Executors.newScheduledThreadPool(10);
    }

    public static void async(final Consumer<GRunnable> task, String... threadName) {
        if (executor == null) {
            new Task();
        }

        executor.submit(new GRunnable() {
            @Override
            public void run() {
                if (threadName.length >= 1) {
                    Thread.currentThread().setName(threadName[0]);
                }

                task.accept(this);
            }
        });
    }

    public static void async(final GRunnable task, String... threadName) {
        async(r -> task.run(), threadName);
    }

    public static void sync(final Consumer<GRunnable> task) {
        main.queue(new GRunnable() {
            @Override
            public void run() {
                task.accept(this);
            }
        });
    }

    public static void scheduleAsync(final Consumer<GRunnable> task, long period, TimeUnit timeUnit) {
        scheduledExecutor.schedule(new GRunnable() {
            @Override
            public void run() {
                task.accept(this);
            }
        }, period, timeUnit);
    }

    public static void scheduleAsync(final GRunnable task, long period, TimeUnit timeUnit) {
        scheduleAsync(r -> task.run(), period, timeUnit);
    }
//
//    public static void scheduleSync(final Consumer<GRunnable> task, long initialDelay, long period, TimeUnit timeUnit) {
//        scheduleAsync(r -> sync(task), initialDelay, period, timeUnit);
//    }

    public static void shutdown() {
        executor.shutdownNow();
    }
}
