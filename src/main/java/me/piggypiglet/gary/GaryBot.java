package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.handlers.CommandHandler;
import me.piggypiglet.gary.core.objects.Config;
import me.piggypiglet.gary.core.tasks.RunTasks;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GaryBot {
    @Inject private CommandHandler commandHandler;
    @Inject private Config config;
    @Inject private RunTasks runTasks;
//    @Inject private MySQLSetup mySQLSetup;

    private GaryBot() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);

//        mySQLSetup.connect();

        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(config.getItem("token"))
                    .setGame(Game.watching("https://garys.life"))
                    .addEventListener(commandHandler)
                    .buildBlocking();
            runTasks.setup(jda);
            runTasks.runTasks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new GaryBot();
    }

}