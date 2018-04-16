package me.piggypiglet.gary.core.framework;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public abstract class Command {

    private final String name;
    private final String description;
    private final boolean help;
    protected boolean delete = true;

    protected Command() {
        this("null", "null", false);
    }

    protected Command(String name, String description, boolean help) {
        this.name = name;
        this.description = description;
        this.help = help;
    }

    protected abstract void execute(MessageReceivedEvent e, String[] args);

    public void run(MessageReceivedEvent e, String[] args) {
        execute(e, args);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean getDelete() { return delete; }

    public boolean getHelp() {
        return help;
    }

}
