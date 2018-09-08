package me.piggypiglet.gary;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.Task;
import me.piggypiglet.gary.core.ginterface.layers.InterfaceCommands;
import me.piggypiglet.gary.core.ginterface.layers.add.AddCommands;
import me.piggypiglet.gary.core.ginterface.layers.clear.ClearCommands;
import me.piggypiglet.gary.core.ginterface.layers.clear.types.Paginations;
import me.piggypiglet.gary.core.handlers.EventHandler;
import me.piggypiglet.gary.core.handlers.ShutdownHandler;
import me.piggypiglet.gary.core.handlers.chat.InterfaceHandler;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
import me.piggypiglet.gary.core.objects.enums.Registerables;
import me.piggypiglet.gary.core.storage.json.FileConfiguration;
import me.piggypiglet.gary.core.storage.json.GFile;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import java.util.Scanner;
import java.util.stream.Stream;

import static me.piggypiglet.gary.core.objects.enums.Registerables.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GaryBot {
    @Inject private GFile gFile;

    @Inject private EventHandler eventHandler;
    @Inject private ShutdownHandler shutdownHandler;
    @Inject private PaginationHandler paginationHandler;
    @Inject private InterfaceHandler interfaceHandler;

    @Inject private InterfaceCommands interfaceCommands;

    @Inject private ClearCommands clearCommands;
    @Inject private AddCommands addCommands;

    @Inject private Paginations clearPaginations;

    private JDA jda;

    void start() {
        Stream.of(
                FILES, EVENTS, INTERFACE, COMMANDS, LOGGERS, MYSQL, BOT, SHUTDOWN
        ).forEach(this::register);
    }

    private void register(Registerables registerable) {
        switch (registerable) {
            case FILES:
                Stream.of(
                        "config"
                ).forEach(f -> gFile.make(f, "./" + f + ".json", "/" + f + ".json"));

                break;

            case EVENTS:
                Stream.of(
                        paginationHandler, interfaceHandler
                ).forEach(eventHandler.getEvents()::add);

                break;

            case INTERFACE:
                // command types
                Stream.of(
                        clearCommands, addCommands
                ).forEach(commands -> interfaceHandler.getTopCommands().put(commands.getType(), commands));

                // commands
                Stream.of(
                        clearPaginations
                ).forEach(interfaceCommands.getInterfaceAbstractList()::add);
                interfaceCommands.sort();

                break;

            case COMMANDS:
                break;

            case LOGGERS:
                break;

            case MYSQL:
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

                Task.async((e) -> {
                    if (input.nextLine().equalsIgnoreCase("stop")) {
                        System.exit(0);
                    }
                });

                break;
        }
    }

    public JDA getJDA() {
        return jda;
    }
}