package me.piggypiglet.gary;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.Task;
import me.piggypiglet.gary.core.objects.enums.Registerables;
import me.piggypiglet.gary.core.storage.json.GFile;

import java.util.stream.Stream;

import static me.piggypiglet.gary.core.objects.enums.Registerables.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
final class GaryBot {
    @Inject private GFile gFile;

    void start() {
        Stream.of(
                FILES, INTERFACE, COMMANDS, LOGGERS, MYSQL, BOT
        ).forEach(this::register);
    }

    private void register(Registerables registerable) {
        switch (registerable) {
            case FILES:
                gFile.make("test", "./test.json", "/test.json");
                break;

            case INTERFACE:
                Task.async((e) -> {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    System.out.println(gFile.getFileConfiguration("test").getInt("item.test"));

                    try {
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    gFile.save("test");
                });

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