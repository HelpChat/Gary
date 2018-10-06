package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.ginterface.layers.InterfaceCommands;
import me.piggypiglet.gary.core.ginterface.layers.add.AddCommands;
import me.piggypiglet.gary.core.ginterface.layers.add.types.AddQuestionnaire;
import me.piggypiglet.gary.core.ginterface.layers.clear.ClearCommands;
import me.piggypiglet.gary.core.ginterface.layers.clear.types.Paginations;
import me.piggypiglet.gary.core.ginterface.layers.run.RunCommands;
import me.piggypiglet.gary.core.ginterface.layers.run.types.RunQuestionnaire;
import me.piggypiglet.gary.core.handlers.EventHandler;
import me.piggypiglet.gary.core.handlers.ShutdownHandler;
import me.piggypiglet.gary.core.handlers.chat.InterfaceHandler;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
import me.piggypiglet.gary.core.objects.enums.Registerables;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;
import me.piggypiglet.gary.core.objects.tasks.GRunnable;
import me.piggypiglet.gary.core.objects.tasks.Task;
import me.piggypiglet.gary.core.storage.json.FileConfiguration;
import me.piggypiglet.gary.core.storage.json.GFile;
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

    @Inject private EventHandler eventHandler;
    @Inject private ShutdownHandler shutdownHandler;
    @Inject private PaginationHandler paginationHandler;
    @Inject private InterfaceHandler interfaceHandler;

    @Inject private InterfaceCommands interfaceCommands;

    @Inject private ClearCommands clearCommands;
    @Inject private AddCommands addCommands;
    @Inject private RunCommands runCommands;

    @Inject private Paginations clearPaginations;

    @Inject private AddQuestionnaire addQuestionnaire;

    @Inject private RunQuestionnaire runQuestionnaire;

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
                        clearCommands, addCommands, runCommands
                ).forEach(commands -> interfaceHandler.getTopCommands().put(commands.getType(), commands));

                // commands
                Stream.of(
                        clearPaginations, addQuestionnaire, runQuestionnaire
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
                }, "Console Command Monitor");

                break;

            case TEST:
//                Task.async((e) -> {
//                    e.sleep(5000);
//
//                    Guild guild = jda.getGuildById(Constants.GUILD);
//                    Emote tp1 = guild.getEmoteById("495977680454549516"), tp2 = guild.getEmoteById("263994827979489290");
//
//                    questionnaires.put("test", new QuestionnaireBuilder(guild.getMemberById(181675431362035712L), jda.getTextChannelById(411094432402636802L)).addQuestions(
//                            new Question("food", "What's your favourite food?", QuestionType.STRING),
//                            new Question("pigs", ":pig: or :pig2:?", QuestionType.EMOTE).setEmotes(jda, "\uD83D\uDC37", "\uD83D\uDC16"),
//                            new Question("testplugins", tp1.getAsMention() + " or " + tp2.getAsMention() + "?", QuestionType.EMOTE).setEmotes(jda, tp1, tp2)
//                    ));
//                });

                break;
        }
    }

    public void queue(GRunnable gRunnable) {
        queue.add(gRunnable);
    }
}