package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.ginterface.layers.standalone.EvalCommand;
import me.piggypiglet.gary.core.handlers.EventHandler;
import me.piggypiglet.gary.core.handlers.ShutdownHandler;
import me.piggypiglet.gary.core.handlers.chat.InterfaceHandler;
import me.piggypiglet.gary.core.handlers.chat.ServiceHandler;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
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
    @Getter private Map<String, QuestionnaireBuilder> questionnaires;

    @Inject private GFile gFile;
    @Inject private MySQLInitializer mySQLInitializer;

    @Inject private EventHandler eventHandler;
    @Inject private ShutdownHandler shutdownHandler;
    @Inject private PaginationHandler paginationHandler;
    @Inject private InterfaceHandler interfaceHandler;
    @Inject private ServiceHandler serviceHandler;

    @Inject private EvalCommand evalCommand;

    private BlockingQueue<GRunnable> queue;
    @Getter private JDA jda;

    void start() {
        questionnaires = new ConcurrentHashMap<>();
        queue = new LinkedBlockingQueue<>();

        Task.async((g) -> Stream.of(
                FILES, EVENTS, INTERFACE, COMMANDS, LOGGERS, MYSQL, BOT, SHUTDOWN, TEST
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
                Stream.of(
                        paginationHandler, interfaceHandler, serviceHandler
                ).forEach(eventHandler.getEvents()::add);

                break;

            case INTERFACE:
                // commands
                Stream.of(
                        evalCommand
                ).forEach(interfaceHandler.getCommands()::add);

                break;

            case COMMANDS:
                break;

            case LOGGERS:
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