package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.commands.admin.BanCheck;
import me.piggypiglet.gary.commands.admin.Speak;
import me.piggypiglet.gary.commands.admin.channel.PurgeChannel;
import me.piggypiglet.gary.commands.admin.channel.SetMotd;
import me.piggypiglet.gary.commands.admin.database.CheckUsers;
import me.piggypiglet.gary.commands.admin.database.SyncUsers;
import me.piggypiglet.gary.commands.chatreaction.admin.SetWord;
import me.piggypiglet.gary.commands.chatreaction.admin.Skip;
import me.piggypiglet.gary.commands.misc.AI;
import me.piggypiglet.gary.commands.misc.RoleID;
import me.piggypiglet.gary.commands.misc.Suggestion;
import me.piggypiglet.gary.commands.placeholderapi.ExpansionInfo;
import me.piggypiglet.gary.commands.server.Info;
import me.piggypiglet.gary.commands.server.help.Commands;
import me.piggypiglet.gary.commands.server.help.Help;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.handlers.*;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.storage.MySQL;
import me.piggypiglet.gary.core.tasks.RunTasks;
import me.piggypiglet.gary.core.utils.misc.LogUtils;
import me.piggypiglet.gary.core.utils.mysql.UserUtils;
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
    @Inject private UserHandler userHandler;
    @Inject private ChatReaction chatReaction;
    @Inject private GFile files;
    @Inject private RunTasks runTasks;
    @Inject private MySQL mysql;
    @Inject private UserUtils userUtils;
    @Inject private ShutdownHandler shutdownHandler;
    @Inject private LoggingHandler loggingHandler;
    @Inject private LogUtils logUtils;

    @Inject private Skip skip;
    @Inject private ExpansionInfo expansionInfo;
    @Inject private AI ai;
    @Inject private BanCheck banCheck;
    @Inject private RoleID roleID;
    @Inject private Speak speak;
    @Inject private Suggestion suggestion;
    @Inject private PurgeChannel purgeChannel;
    @Inject private SetWord setWord;
    @Inject private Info serverInfo;
    @Inject private CheckUsers checkUsers;
    @Inject private SyncUsers syncUsers;
    @Inject private SetMotd setMotd;
    @Inject private Help help;
    @Inject private Commands commands;

    private JDA jda;

    private GaryBot() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        Stream.of(
                "files", "commands", "bot", "mysql", "tasks"
        ).forEach(this::register);

        Runtime.getRuntime().addShutdownHook(shutdownHandler);
    }

    private void register(String register) {
        try {
            switch (register.toLowerCase()) {
                case "commands":
                    Stream.of(
                            skip, expansionInfo, ai, banCheck, roleID, speak, suggestion, purgeChannel, serverInfo,
                            checkUsers, syncUsers, setMotd, help, commands, setWord
                    ).forEach(commandHandler.getCommands()::add);
                    break;
                case "files":
                    files.make("config", "./config.json", "/config.json");
                    files.make("words", "./words.txt", "/words.txt");
                    files.make("users", "schema/Users.sql", "/schema/Users.sql");
                    files.make("stats", "schema/Stats.sql", "/schema/Stats.sql");
                    files.make("messages", "schema/Messages.sql", "/schema/Messages.sql");
                    chatReaction.loadWords();
                    break;
                case "tasks":
                    runTasks.setup(jda);
                    runTasks.runTasks();
                    break;
                case "bot":
                    jda = new JDABuilder(AccountType.BOT)
                            .setToken(files.getItem("config", "token"))
                            .setGame(Game.watching("https://garys.life"))
                            .addEventListener(userHandler)
                            .addEventListener(commandHandler)
                            .addEventListener(chatHandler)
                            .addEventListener(loggingHandler)
                            .buildBlocking();
                    logUtils.setup(jda);
                    break;
                case "mysql":
                    mysql.connect();
                    mysql.setup(jda);
                    System.out.println(userUtils.syncUsers(jda.getGuildById(Constants.HELP_CHAT), jda));
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