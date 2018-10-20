package me.piggypiglet.gary.core.framework.ginterface;

import lombok.Getter;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class Command {
    @Getter private final List<String> commands = new ArrayList<>();
    @Getter protected String argPattern;

    protected Command(String... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    protected abstract void execute(GuildMessageReceivedEvent e, String[] args);

    public void run(GuildMessageReceivedEvent e, String[] args) {
        execute(e, args);
    }
}
