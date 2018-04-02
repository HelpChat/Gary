package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.aprilfirst.handlers.ChatHandler;
import me.piggypiglet.gary.commands.*;
import me.piggypiglet.gary.commands.admin.PurgeChannel;
import me.piggypiglet.gary.commands.admin.Restart;
import me.piggypiglet.gary.commands.chatreaction.CurrentWord;
import me.piggypiglet.gary.commands.chatreaction.NewWord;
import me.piggypiglet.gary.commands.placeholderapi.Info;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.handlers.CommandHandler;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.storage.MySQLSetup;
import me.piggypiglet.gary.core.tasks.RunTasks;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GaryBot {
    @Inject private CommandHandler commandHandler;
    @Inject private ChatHandler chatHandler;
    @Inject private GFile files;
    @Inject private RunTasks runTasks;
    @Inject private MySQLSetup mysql;

    @Inject private CurrentWord currentWord;
    @Inject private Info info;
    @Inject private AI ai;
    @Inject private BanCheck banCheck;
    @Inject private RoleID roleID;
    @Inject private Speak speak;
    @Inject private Suggestion suggestion;
    @Inject private PurgeChannel purgeChannel;
    @Inject private NewWord newWord;
    @Inject private Restart restart;

    private JDA jda;

    private GaryBot() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        Stream.of(
                "files", "commands", "bot", "tasks"
        ).forEach(this::register);
    }

    private void register(String register) {
        try {
            switch (register.toLowerCase()) {
                case "commands":
                    Stream.of(
                            currentWord, info, ai, banCheck, roleID, speak, suggestion, purgeChannel, newWord, restart
                    ).forEach(commandHandler.getCommands()::add);
                    break;
                case "files":
                    files.make("config", "./config.json", "/config.json");
                    files.make("words", "./words.txt", "/words.txt");
                    files.make("word-storage", "./word-storage.json", "/word-storage.json");
                    files.make("mysql", "./schema/GaryBot.sql", "/schema/GaryBot.sql");
                    break;
                case "tasks":
                    runTasks.setup(jda);
                    runTasks.runTasks();
                    break;
                case "bot":
                    jda = new JDABuilder(AccountType.BOT)
                            .setToken(files.getItem("config", "token"))
                            .setGame(Game.watching("https://garys.life"))
                            .addEventListener(commandHandler)
                            .addEventListener(chatHandler)
                            .buildBlocking();
                    break;
                case "mysql":
                    mysql.connect();
                    mysql.setup();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new GaryBot();
    }

}