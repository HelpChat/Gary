package me.piggypiglet.gary;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.Task;
import me.piggypiglet.gary.core.objects.enums.Registerables;
import me.piggypiglet.gary.core.storage.json.GFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private String test(String test) {
        return null;
    }

    private void register(Registerables registerable) {
        switch (registerable) {
            case FILES:
                gFile.make("test", "./test.json", "/test.json");
                break;

            case INTERFACE:
                gFile.getFileConfiguration("test").get("");

                Task.async((e) -> {
                    e.sleep(2000);
                    System.out.println(gFile.getFileConfiguration("test").getInt("item.test"));
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