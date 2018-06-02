package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.commands.admin.BanCheck;
import me.piggypiglet.gary.commands.admin.Eval;
import me.piggypiglet.gary.commands.admin.Speak;
import me.piggypiglet.gary.commands.admin.channel.PurgeChannel;
import me.piggypiglet.gary.commands.admin.channel.SetMotd;
import me.piggypiglet.gary.commands.admin.database.CheckUsers;
import me.piggypiglet.gary.commands.admin.database.SyncUsers;
import me.piggypiglet.gary.commands.chatreaction.admin.SetWord;
import me.piggypiglet.gary.commands.chatreaction.admin.Skip;
import me.piggypiglet.gary.commands.experimental.Interface;
import me.piggypiglet.gary.commands.misc.AI;
import me.piggypiglet.gary.commands.misc.RoleID;
import me.piggypiglet.gary.commands.misc.Suggestion;
import me.piggypiglet.gary.commands.placeholderapi.ExpansionInfo;
import me.piggypiglet.gary.commands.server.Info;
import me.piggypiglet.gary.commands.server.help.Commands;
import me.piggypiglet.gary.commands.server.help.Help;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.handlers.*;
import me.piggypiglet.gary.core.logging.types.*;
import me.piggypiglet.gary.core.storage.json.GFile;
import me.piggypiglet.gary.core.storage.json.GTypes;
import me.piggypiglet.gary.core.storage.mysql.MySQL;
import me.piggypiglet.gary.core.tasks.RunTasks;
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
    @Inject private GTypes gTypes;
    @Inject private RunTasks runTasks;
    @Inject private MySQL mysql;
    @Inject private UserUtils userUtils;
    @Inject private ShutdownHandler shutdownHandler;
    @Inject private LoggingHandler loggingHandler;
    @Inject private PaginationHandler paginationHandler;

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
    @Inject private Eval eval;
    @Inject private Interface interfaceCMD;

    @Inject private MemberJoin memberJoin;
    @Inject private MemberLeave memberLeave;
    @Inject private MemberBan memberBan;
    @Inject private MessageEdit messageEdit;
    @Inject private MessageDelete messageDelete;
    @Inject private MessageBulkDelete messageBulkDelete;
    @Inject private VoiceJoin voiceJoin;

    private JDA jda;

    private GaryBot() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        Stream.of(
                "files", "commands", "loggers", "bot", "mysql", "tasks"
        ).forEach(this::register);

        Runtime.getRuntime().addShutdownHook(shutdownHandler);
    }

    private void register(String register) {
        try {
            switch (register.toLowerCase()) {
                case "commands":
                    Stream.of(
                            skip, expansionInfo, ai, banCheck, roleID, speak, suggestion, purgeChannel, serverInfo, eval,
                            checkUsers, syncUsers, setMotd, help, commands, setWord, interfaceCMD
                    ).forEach(commandHandler.getCommands()::add);
                    break;

                case "loggers":
                    Stream.of(
                            memberJoin, memberLeave, memberBan, messageEdit, messageDelete, messageBulkDelete, voiceJoin
                    ).forEach(loggingHandler.getLoggers()::add);

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
                            .setToken(gTypes.getString("config", "token"))
                            .setGame(Game.watching("https://garys.life"))
                            .setBulkDeleteSplittingEnabled(false)
                            .addEventListener(userHandler, commandHandler, chatHandler, loggingHandler, paginationHandler)
                            .buildBlocking();
                    break;

                case "mysql":
                    mysql.connect(jda);
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