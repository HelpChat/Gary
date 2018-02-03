package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.handlers.CommandHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class Main {
    @Inject
    private CommandHandler commandHandler;
    private Main() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        Main main = new Main();
        new JDABuilder(AccountType.BOT)
                .setToken("")
                .setGame(Game.of(Game.GameType.WATCHING, "https://garys.life"))
                .addEventListener(main.commandHandler)
                .buildBlocking();
    }
}