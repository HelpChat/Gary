package me.piggypiglet.gary;

import me.piggypiglet.gary.core.objects.enums.Registerables;

import java.util.stream.Stream;

import static me.piggypiglet.gary.core.objects.enums.Registerables.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
final class GaryBot {
    void start() {
        Stream.of(
                FILES, INTERFACE, COMMANDS, LOGGERS, MYSQL, BOT
        ).forEach(this::register);
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