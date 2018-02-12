package me.piggypiglet.gary.commands;

import me.piggypiglet.gary.core.framework.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Suggestion extends Command {
    public Suggestion() {
        super("?suggestion");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (args.length == 1) {

        }
    }
}
