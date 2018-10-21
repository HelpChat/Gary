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
import me.piggypiglet.gary.core.handlers.chat.InterfaceHandler;
import me.piggypiglet.gary.core.handlers.misc.LoggingHandler;
import me.piggypiglet.gary.core.objects.enums.Registerables;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;
import me.piggypiglet.gary.core.objects.tasks.GRunnable;
import me.piggypiglet.gary.core.objects.tasks.Task;
import me.piggypiglet.gary.core.storage.json.FileConfiguration;
import me.piggypiglet.gary.core.storage.json.GFile;
import me.piggypiglet.gary.core.storage.mysql.MySQLInitializer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import static me.piggypiglet.gary.core.objects.enums.Registerables.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class GaryBot {
    private final BlockingQueue<GRunnable> queue = new LinkedBlockingQueue<>();
    @Getter private final Map<String, QuestionnaireBuilder> questionnaires = new ConcurrentHashMap<>();
    @Getter private JDA jda;
    @Getter private Injector injector;
    private final Reflections reflections = new Reflections("me.piggypiglet.gary");

    @Inject private GFile gFile;
    @Inject private MySQLInitializer mySQLInitializer;

    @Inject private EventHandler eventHandler;
    @Inject private ShutdownHandler shutdownHandler;
    @Inject private InterfaceHandler interfaceHandler;
    @Inject private LoggingHandler loggingHandler;

    void start(Injector injector) {
        this.injector = injector;

        Task.async((g) -> Stream.of(
                FILES, EVENTS, INTERFACE, LOGGERS, MYSQL, BOT, SHUTDOWN, TEST
        ).forEach(GaryBot.this::register), "Gary");

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
                        "config.json", "schema.sql"
                ).forEach(f -> gFile.make(f, "./" + f, "/" + f));
                break;

            case EVENTS:
                reflections.getSubTypesOf(GEvent.class).stream().map(injector::getInstance).forEach(eventHandler.getEvents()::add);
                break;

            case INTERFACE:
                reflections.getSubTypesOf(Command.class).stream().map(injector::getInstance).forEach(interfaceHandler.getCommands()::add);
                break;

            case LOGGERS:
                reflections.getSubTypesOf(Logger.class).stream().map(injector::getInstance).forEach(loggingHandler.getLoggers()::add);
                break;

            case MYSQL:
                mySQLInitializer.connect();
                break;

            case BOT:
                final FileConfiguration config = gFile.getFileConfiguration("config");

                try {
                    jda = new JDABuilder(AccountType.BOT)
                            .setToken(config.getString("token"))
                            .setGame(Game.of(Game.GameType.valueOf(config.getString("game.type", "default").toUpperCase()), config.getString("game.game", "https://gary.helpch.at")))
                            .addEventListener(eventHandler)
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case SHUTDOWN:
                Runtime.getRuntime().addShutdownHook(shutdownHandler);

                Scanner input = new Scanner(System.in);

                Task.async(r -> {
                    if (input.nextLine().equalsIgnoreCase("stop")) {
                        System.exit(0);
                    }
                }, "Console Command Monitor");
                break;

            case TEST:
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
}