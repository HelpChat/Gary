package me.piggypiglet.gary.core;

import java.util.function.Consumer;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Task {
    public static void async(final Consumer<Runnable> task, String... threadName) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                task.accept(this);
            }
        });

        if (threadName.length <= 1) {
            thread.setName(threadName[0]);
        }

        thread.start();
    }
}
