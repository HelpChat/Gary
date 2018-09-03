package me.piggypiglet.gary;

import com.google.inject.Inject;
import me.piggypiglet.gary.commands.admin.BanCheck;
import me.piggypiglet.gary.commands.admin.Eval;
import me.piggypiglet.gary.commands.admin.Giveaway;
import me.piggypiglet.gary.commands.admin.Speak;
import me.piggypiglet.gary.commands.admin.channel.PurgeChannel;
import me.piggypiglet.gary.commands.admin.channel.SetMotd;
import me.piggypiglet.gary.commands.admin.database.CheckUsers;
import me.piggypiglet.gary.commands.admin.database.SyncUsers;
import me.piggypiglet.gary.commands.admin.responses.AddResponse;
import me.piggypiglet.gary.commands.admin.responses.GetResponse;
import me.piggypiglet.gary.commands.admin.responses.RemoveResponse;
import me.piggypiglet.gary.commands.chatreaction.admin.SetWord;
import me.piggypiglet.gary.commands.chatreaction.admin.Skip;
import me.piggypiglet.gary.commands.misc.AI;
import me.piggypiglet.gary.commands.misc.Plugin;
import me.piggypiglet.gary.commands.misc.RoleID;
import me.piggypiglet.gary.commands.misc.Suggestion;
import me.piggypiglet.gary.commands.placeholderapi.ExpansionInfo;
import me.piggypiglet.gary.commands.server.Info;
import me.piggypiglet.gary.commands.server.help.Commands;
import me.piggypiglet.gary.commands.server.help.Help;
import me.piggypiglet.gary.core.ginterface.layers.InterfaceCommands;
import me.piggypiglet.gary.core.ginterface.layers.add.AddCommands;
import me.piggypiglet.gary.core.ginterface.layers.clear.ClearCommands;
import me.piggypiglet.gary.core.ginterface.layers.clear.types.Paginations;
import me.piggypiglet.gary.core.handlers.EventHandler;
import me.piggypiglet.gary.core.handlers.ShutdownHandler;
import me.piggypiglet.gary.core.handlers.chat.CommandHandler;
import me.piggypiglet.gary.core.handlers.chat.InterfaceHandler;
import me.piggypiglet.gary.core.handlers.misc.GiveawayHandler;
import me.piggypiglet.gary.core.handlers.misc.LoggingHandler;
import me.piggypiglet.gary.core.logging.types.*;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.Registerables;
import me.piggypiglet.gary.core.storage.json.GFile;
import me.piggypiglet.gary.core.storage.json.GTypes;
import me.piggypiglet.gary.core.storage.mysql.MySQL;
import me.piggypiglet.gary.core.tasks.RunTasks;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import java.util.stream.Stream;

import static me.piggypiglet.gary.core.objects.enums.Registerables.*;

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
    @Inject private ShutdownHandler shutdownHandler;
    @Inject private CommandHandler commandHandler;
    @Inject private EventHandler eventHandler;
    @Inject private GiveawayHandler giveawayHandler;
    @Inject private InterfaceCommands interfaceCommands;
    @Inject private InterfaceHandler interfaceHandler;
    @Inject private LoggingHandler loggingHandler;

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
    @Inject private Plugin plugin;
    @Inject private AddResponse addResponse;
    @Inject private RemoveResponse removeResponse;
    @Inject private GetResponse getResponse;
//    @Inject private ListResponses listResponses;

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

    private JDA jda;

    void start() {
        Stream.of(
                FILES, INTERFACE, COMMANDS, LOGGERS, BOT, MYSQL, TASKS
        ).forEach(this::register);

        Runtime.getRuntime().addShutdownHook(shutdownHandler);
    }

    private void register(Registerables registerable) {
        try {
            switch (registerable) {
                case FILES:
                    files.make("config", "./config.json", "/config.json");
                    files.make("words", "./words.txt", "/words.txt");
                    files.make("users", "schema/Users.sql", "/schema/Users.sql");
                    files.make("stats", "schema/Stats.sql", "/schema/Stats.sql");
                    files.make("messages", "schema/Messages.sql", "/schema/Messages.sql");
                    files.make("giveaways", "schema/Giveaways.sql", "/schema/Giveaways.sql");
                    files.make("giveaways_users", "schema/GiveawaysUsers.sql", "/schema/GiveawaysUsers.sql");
                    files.make("faq", "schema/Faq.sql", "/schema/Faq.sql");
                    chatReaction.loadWords();

                    break;

                case INTERFACE:
                    // command types, top level
                    Stream.of(
                            clearCommands, addCommands
                    ).forEach(item -> interfaceHandler.getTopCommands().put(item.getType(), item));

                    // commands
                    Stream.of(
                            interfacePaginations
                    ).forEach(interfaceCommands.getInterfaceAbstractList()::add);
                    interfaceCommands.sort();

                    break;

                case COMMANDS:
                    Stream.of(
                            skip, expansionInfo, ai, banCheck, roleID, speak, suggestion, purgeChannel, serverInfo, eval,
                            checkUsers, syncUsers, setMotd, help, commands, setWord, giveaway, plugin,
                            skip, expansionInfo, /*ai,*/ banCheck, roleID, speak, suggestion, purgeChannel, serverInfo, eval,
                            checkUsers, syncUsers, setMotd, help, commands, setWord, giveaway, addResponse, removeResponse, getResponse
//                            listResponses
                    ).forEach(commandHandler.getCommands()::add);

                    break;

                case LOGGERS:
                    Stream.of(
                            memberJoin, memberLeave, memberBan, messageEdit, messageDelete, messageBulkDelete, voiceJoin
                    ).forEach(loggingHandler.getLoggers()::add);

                    break;

                case BOT:
                    jda = new JDABuilder(AccountType.BOT)
                            .setToken(gTypes.getString("config", "token"))
                            .setGame(Game.watching("https://gary.helpch.at"))
                            .setBulkDeleteSplittingEnabled(false)
                            .addEventListener(eventHandler)
                            .build();
                    // We build on the main thread to prevent synchronization issues.

                    break;

                case MYSQL:
                    mysql.connect();

                    break;

                case TASKS:
                    runTasks.runTasks();
                    giveawayHandler.update(jda.getTextChannelById(Constants.GIVEAWAY_CHANNEL));

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JDA getJda() {
        return jda;
    }
}