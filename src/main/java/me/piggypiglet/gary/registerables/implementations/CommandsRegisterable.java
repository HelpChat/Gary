package me.piggypiglet.gary.registerables.implementations;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.commands.Command;
import me.piggypiglet.gary.commands.CommandHandler;
import me.piggypiglet.gary.registerables.Registerable;
import net.dv8tion.jda.api.JDA;
import org.reflections.Reflections;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class CommandsRegisterable extends Registerable {
    @Inject private JDA jda;
    @Inject private CommandHandler commandHandler;
    @Inject private Reflections reflections;
    @Inject private Injector injector;

    @Override
    protected void execute() {
        jda.addEventListener(commandHandler);
        reflections.getSubTypesOf(Command.class).stream().map(injector::getInstance).forEach(commandHandler.getCommands()::add);
    }
}
