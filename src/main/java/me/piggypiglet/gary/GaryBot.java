package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Injector;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.handlers.CommandHandler;
import me.piggypiglet.gary.core.objects.Constants;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GaryBot {

    @Inject private CommandHandler commandHandler;

    private GaryBot() throws LoginException, InterruptedException {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        new JDABuilder(AccountType.BOT)
          .setToken(Constants.BOT_TOKEN)
          .setGame(Game.of(Game.GameType.WATCHING, "https://garys.life"))
          .addEventListener(commandHandler)
          .buildBlocking();
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        new GaryBot();
    }

}