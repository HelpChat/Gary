package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.commands.admin.BanCheck;
import me.piggypiglet.gary.commands.admin.Eval;
import me.piggypiglet.gary.commands.admin.Giveaway;
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
import me.piggypiglet.gary.core.ginterface.layers.InterfaceCommands;
import me.piggypiglet.gary.core.ginterface.layers.add.AddCommands;
import me.piggypiglet.gary.core.ginterface.layers.add.types.Reaction;
import me.piggypiglet.gary.core.ginterface.layers.clear.ClearCommands;
import me.piggypiglet.gary.core.ginterface.layers.clear.types.Paginations;
import me.piggypiglet.gary.core.handlers.*;
import me.piggypiglet.gary.core.logging.types.*;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.storage.json.GFile;
import me.piggypiglet.gary.core.storage.json.GTypes;
import me.piggypiglet.gary.core.storage.mysql.MySQL;
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
    // Imagine how much worse the bottom eye bleed would be if I used any other methods of getting class instances. Be happy with the following.

    @Inject private ChatReaction chatReaction;
    @Inject private GFile files;
    @Inject private GTypes gTypes;
    @Inject private RunTasks runTasks;
    @Inject private MySQL mysql;

    // Technically, these could be combined into 1 class, but we're following oop. I might use a more abstract solution in the future.
    @Inject private CommandHandler commandHandler;
    @Inject private ChatHandler chatHandler;
    @Inject private UserHandler userHandler;
    @Inject private ShutdownHandler shutdownHandler;
    @Inject private LoggingHandler loggingHandler;
    @Inject private PaginationHandler paginationHandler;
    @Inject private InterfaceCommands interfaceCommands;
    @Inject private InterfaceHandler interfaceHandler;
    @Inject private ShowcaseHandler showcaseHandler;
    @Inject private GiveawayHandler giveawayHandler;

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
    @Inject private Giveaway giveaway;

    @Inject private MemberJoin memberJoin;
    @Inject private MemberLeave memberLeave;
    @Inject private MemberBan memberBan;
    @Inject private MessageEdit messageEdit;
    @Inject private MessageDelete messageDelete;
    @Inject private MessageBulkDelete messageBulkDelete;
    @Inject private VoiceJoin voiceJoin;

    @Inject private ClearCommands clearCommands;
    @Inject private AddCommands addCommands;

    @Inject private Paginations interfacePaginations;

    @Inject private Reaction interfaceReaction;

    private JDA jda;

    private GaryBot() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        // Ordered startup of the different shits.
        Stream.of(
                "files", "interface", "commands", "loggers", "bot", "mysql", "tasks"
        ).forEach(this::register);

        Runtime.getRuntime().addShutdownHook(shutdownHandler);
    }

    private void register(String register) {
        try {
            switch (register.toLowerCase()) {
                case "files":
                    files.make("config", "./config.json", "/config.json");
                    files.make("words", "./words.txt", "/words.txt");
                    files.make("users", "schema/Users.sql", "/schema/Users.sql");
                    files.make("stats", "schema/Stats.sql", "/schema/Stats.sql");
                    files.make("messages", "schema/Messages.sql", "/schema/Messages.sql");
                    files.make("giveaways", "schema/Giveaways.sql", "/schema/Giveaways.sql");
                    files.make("giveaways_users", "schema/GiveawaysUsers.sql", "/schema/GiveawaysUsers.sql");
                    chatReaction.loadWords();

                    break;

                case "interface":
                    // command types, top level
                    Stream.of(
                            clearCommands, addCommands
                    ).forEach(item -> interfaceHandler.getTopCommands().put(item.getType(), item));

                    // commands
                    Stream.of(
                            interfacePaginations, interfaceReaction
                    ).forEach(interfaceCommands.getInterfaceAbstractList()::add);
                    interfaceCommands.sort();

                    break;

                case "commands":
                    Stream.of(
                            skip, expansionInfo, ai, banCheck, roleID, speak, suggestion, purgeChannel, serverInfo, eval,
                            checkUsers, syncUsers, setMotd, help, commands, setWord, giveaway
                    ).forEach(commandHandler.getCommands()::add);

                    break;

                case "loggers":
                    Stream.of(
                            memberJoin, memberLeave, memberBan, messageEdit, messageDelete, messageBulkDelete, voiceJoin
                    ).forEach(loggingHandler.getLoggers()::add);

                    break;

                case "bot":
                    jda = new JDABuilder(AccountType.BOT)
                            .setToken(gTypes.getString("config", "token"))
                            .setGame(Game.watching("https://garys.life"))
                            .setBulkDeleteSplittingEnabled(false)
                            .addEventListener(
                                    userHandler, commandHandler, chatHandler, loggingHandler, paginationHandler, interfaceHandler,
                                    showcaseHandler, giveawayHandler
                            )
                            .buildBlocking();
                    // We build on the main thread to prevent synchronization issues (in other words i'm lazy).

                    break;

                case "mysql":
                    mysql.connect(jda);

                    break;

                case "tasks":
                    // As much as I love guice, it basically ruins the idea of using a constructor so I'm stuck with these lame ass setup methods you'll see nearly everywhere.
                    runTasks.setup(jda);
                    runTasks.runTasks();
                    giveawayHandler.update(jda.getTextChannelById(Constants.GIVEAWAY_CHANNEL));

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