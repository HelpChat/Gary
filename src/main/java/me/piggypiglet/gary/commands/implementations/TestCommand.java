package me.piggypiglet.gary.commands.implementations;

import me.piggypiglet.gary.commands.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class TestCommand extends Command {
    public TestCommand() {
        super("test");
        options.setPermission("test");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage("it works").queue();
    }
}
