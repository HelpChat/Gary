package me.piggypiglet.gary.core.objects.tasks;

import me.piggypiglet.gary.GaryBot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Task {
    private static ExecutorService executor;

    private Task() {
        executor = Executors.newFixedThreadPool(50);
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

    public static void sync(final Consumer<GRunnable> task, GaryBot main) {
        main.queue(new GRunnable() {
            @Override
            public void run() {
                task.accept(this);
            }
        });
    }

    public static void shutdown() {
        executor.shutdownNow();
    }
}
