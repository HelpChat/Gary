package me.piggypiglet.gary.core.framework;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public abstract class Command {

    private final String name;

    protected Command() {
        this("null");
    }

    protected Command(String name) {
        this.name = name;
    }

    protected abstract void execute(MessageReceivedEvent e, String[] args);

    public void run(MessageReceivedEvent e, String[] args) {
        execute(e, args);
    }

    public String getName() {
        return name;
    }

}
