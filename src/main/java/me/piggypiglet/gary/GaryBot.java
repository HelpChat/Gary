package me.piggypiglet.gary;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.ginterface.layers.InterfaceCommands;
import me.piggypiglet.gary.core.ginterface.layers.add.AddCommands;
import me.piggypiglet.gary.core.ginterface.layers.clear.ClearCommands;
import me.piggypiglet.gary.core.ginterface.layers.clear.types.Paginations;
import me.piggypiglet.gary.core.handlers.EventHandler;
import me.piggypiglet.gary.core.handlers.ShutdownHandler;
import me.piggypiglet.gary.core.handlers.chat.InterfaceHandler;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
import me.piggypiglet.gary.core.objects.enums.Registerables;
import me.piggypiglet.gary.core.objects.questionnaire.Question;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;
import me.piggypiglet.gary.core.objects.tasks.GRunnable;
import me.piggypiglet.gary.core.objects.tasks.Task;
import me.piggypiglet.gary.core.storage.json.FileConfiguration;
import me.piggypiglet.gary.core.storage.json.GFile;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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

    private BlockingQueue<GRunnable> queue;
    private JDA jda;

    void start() {
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
                        List<String> threads = new ArrayList<>();
                        new ArrayList<>(Thread.getAllStackTraces().keySet()).stream().map(Thread::getName).forEach(threads::add);
                        System.out.println(threads);

                        System.exit(0);
                    }
                }, "Console Command Monitor");

                break;

            case TEST:
                Task.async((e) -> {
                    e.sleep(5000);

                    Guild guild = jda.getGuildById(164280494874165248L);

                    QuestionnaireBuilder builder = new QuestionnaireBuilder(guild.getMemberById(181675431362035712L), jda.getTextChannelById(411094432402636802L)).addQuestions(
                            new Question("food", "What's your favourite food?", "string"),
                            new Question("pigs", ":pig: or :pig2:?", "\uD83D\uDC37", "\uD83D\uDC16"),
                            new Question("testplugins", "<:TestPlugins:263994827979489290> or <:TestPlugins2:375139686542213120>?", guild.getEmoteById(263994827979489290L), guild.getEmoteById(375139686542213120L))
                    );

                    jda.getTextChannelById(411094432402636802L).sendMessage("```\n" + builder.build().getResponses().toString() + "```").queue();

                });

                break;
        }
    }

    public JDA getJDA() {
        return jda;
    }

    public void queue(GRunnable gRunnable) {
        queue.add(gRunnable);
    }
}