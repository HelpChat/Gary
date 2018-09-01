package me.piggypiglet.gary;

import me.piggypiglet.gary.core.Task;
import me.piggypiglet.gary.core.objects.enums.Registerables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GaryBot {
    void start() {
        Logger logger = LoggerFactory.getLogger("test");

        Task.async((e) -> {
            logger.info(Thread.currentThread().getName());
        }, "test");

        Task.async((e) -> {
            logger.info(Thread.currentThread().getName());
        }, "test2");
    }

    private void register(Registerables registerable) {
        switch (registerable) {
            case FILES:
                break;

            case INTERFACE:
                break;

            case COMMANDS:
                break;

            case LOGGERS:
                break;

            case MYSQL:
                break;

            case BOT:
                break;
        }
    }
}