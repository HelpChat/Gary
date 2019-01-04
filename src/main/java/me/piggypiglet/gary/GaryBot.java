package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.framework.logging.Logger;
import me.piggypiglet.gary.core.handlers.EventHandler;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.handlers.ShutdownHandler;
import me.piggypiglet.gary.core.handlers.chat.CommandHandler;
import me.piggypiglet.gary.core.handlers.misc.GiveawayHandler;
import me.piggypiglet.gary.core.handlers.misc.LoggingHandler;
import me.piggypiglet.gary.core.objects.enums.Registerables;
import me.piggypiglet.gary.core.objects.tasks.GRunnable;
import me.piggypiglet.gary.core.objects.tasks.Task;
import me.piggypiglet.gary.core.objects.tasks.tasks.ServiceClear;
import me.piggypiglet.gary.core.storage.file.FileConfiguration;
import me.piggypiglet.gary.core.storage.file.GFile;
import me.piggypiglet.gary.core.storage.mysql.MySQLInitializer;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.reflections.Reflections;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static me.piggypiglet.gary.core.objects.enums.Registerables.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class GaryBot {
    private final BlockingQueue<GRunnable> queue = new LinkedBlockingQueue<>();
    private final Reflections reflections = new Reflections("me.piggypiglet.gary");
    @Getter private JDA jda;
    @Getter private Injector injector;

    @Inject private static GFile sGFile;
    @Inject private GFile gFile;
    @Inject private MySQLInitializer mySQLInitializer;

    @Inject private EventHandler eventHandler;
    @Inject private ShutdownHandler shutdownHandler;
    @Inject private CommandHandler commandHandler;
    @Inject private LoggingHandler loggingHandler;
    @Inject private GiveawayHandler giveawayHandler;

    @Inject private ServiceClear serviceClear;

    void start(Injector injector) {
        this.injector = injector;

        Task.async((g) -> Stream.of(
                FILES, EVENTS, COMMANDS, LOGGERS, BOT, MYSQL, CONSOLE, TASKS
        ).forEach(this::register), "Gary");

        // sacrifice the main thread.
        try {
            //noinspection InfiniteLoopStatement
            while (true) queue.take().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(Registerables registerable) {
        switch (registerable) {
            case FILES:
                Stream.of(
                        "config.json", "schema.sql", "lang.json"
                ).forEach(f -> gFile.make(f, "./" + f, "/" + f, false));

                gFile.make("embed.json", null, "/" + "embed.json", true);
                break;

            case EVENTS:
                Runtime.getRuntime().addShutdownHook(shutdownHandler);
                reflections.getSubTypesOf(GEvent.class).stream().map(injector::getInstance).forEach(eventHandler.getEvents()::add);
                break;

            case COMMANDS:
                reflections.getSubTypesOf(Command.class).stream().map(injector::getInstance).forEach(commandHandler.getCommands()::add);
                break;

            case LOGGERS:
                reflections.getSubTypesOf(Logger.class).stream().map(injector::getInstance).forEach(loggingHandler.getLoggers()::add);
                break;

            case BOT:
                final FileConfiguration config = gFile.getFileConfiguration("config");

                try {
                    jda = new JDABuilder(AccountType.BOT)
                            .setToken(config.getString("token"))
                            .setActivity(Activity.of(Activity.ActivityType.valueOf(config.getString("game.type", "default").toUpperCase()), config.getString("game.game", "https://gary.helpch.at")))
                            .addEventListeners(eventHandler)
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case MYSQL:
                mySQLInitializer.connect();

                Task.scheduleAsync(r -> giveawayHandler.populate(), 3, TimeUnit.SECONDS);
                break;

            case CONSOLE:
                Task.async(r -> {
                    Scanner input = new Scanner(System.in);

                    while (true) {
                        switch (input.nextLine().toLowerCase()) {
                            case "stop": System.exit(0); break;
                            case "clear-channels": Task.async(serviceClear); break;
                        }
                    }
                }, "Console Command Monitor");
                break;

            case TASKS:
//                Task.async(r -> {
//                    r.sleep(10000);
//                });

                break;
        }
    }

    /**
     * Queues a runnable to be ran on the main thread.<br/>
     * <i>This will not block the program if a thread blocking runnable is executed</i>
     * @param gRunnable The runnable to be queued.
     */
    public void queue(GRunnable gRunnable) {
        queue.add(gRunnable);
    }

    public static String getVersion() {
        return sGFile.getFileConfiguration("embed").getString("version");
    }
}
