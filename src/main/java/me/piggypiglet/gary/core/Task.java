package me.piggypiglet.gary.core;

import me.piggypiglet.gary.core.objects.tasks.GRunnable;

import java.util.function.Consumer;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Task {
    public static void async(final Consumer<GRunnable> task, String... threadName) {
        Thread thread = new Thread(new GRunnable() {
            @Override
            public void run() {
                task.accept(this);
            }
        });

        if (threadName.length >= 1) {
            thread.setName(threadName[0]);
        }

        thread.start();
    }
}
