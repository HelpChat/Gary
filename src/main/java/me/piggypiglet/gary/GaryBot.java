package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.commands.admin.BanCheck;
import me.piggypiglet.gary.commands.admin.Speak;
import me.piggypiglet.gary.commands.admin.channel.PurgeChannel;
import me.piggypiglet.gary.commands.admin.channel.SetMotd;
import me.piggypiglet.gary.commands.admin.database.CheckUsers;
import me.piggypiglet.gary.commands.admin.database.SyncUsers;
import me.piggypiglet.gary.commands.chatreaction.CurrentWord;
import me.piggypiglet.gary.commands.chatreaction.NewWord;
import me.piggypiglet.gary.commands.misc.AI;
import me.piggypiglet.gary.commands.misc.RoleID;
import me.piggypiglet.gary.commands.misc.Suggestion;
import me.piggypiglet.gary.commands.placeholderapi.ExpansionInfo;
import me.piggypiglet.gary.commands.server.Info;
import me.piggypiglet.gary.commands.server.help.Commands;
import me.piggypiglet.gary.commands.server.help.Help;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.handlers.ChatHandler;
import me.piggypiglet.gary.core.handlers.CommandHandler;
import me.piggypiglet.gary.core.handlers.UserHandler;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.storage.MySQL;
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
    @Inject private UserHandler userHandler;
    @Inject private ChatReaction chatReaction;
    @Inject private GFile files;
    @Inject private RunTasks runTasks;
    @Inject private MySQL mysql;

    @Inject private CurrentWord currentWord;
    @Inject private ExpansionInfo expansionInfo;
    @Inject private AI ai;
    @Inject private BanCheck banCheck;
    @Inject private RoleID roleID;
    @Inject private Speak speak;
    @Inject private Suggestion suggestion;
    @Inject private PurgeChannel purgeChannel;
    @Inject private NewWord newWord;
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
    }

    private void register(String register) {
        try {
            switch (register.toLowerCase()) {
                case "commands":
                    Stream.of(
                            currentWord, expansionInfo, ai, banCheck, roleID, speak, suggestion, purgeChannel, newWord, serverInfo,
                            checkUsers, syncUsers, setMotd, help, commands
                    ).forEach(commandHandler.getCommands()::add);
                    break;
                case "files":
                    files.make("config", "./config.json", "/config.json");
                    files.make("words", "./words.txt", "/words.txt");
                    files.make("users", "schema/Users.sql", "/schema/Users.sql");
                    files.make("stats", "schema/Stats.sql", "/schema/Stats.sql");
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
                            .buildBlocking();
                    break;
                case "mysql":
                    mysql.connect();
                    mysql.setup(jda);
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