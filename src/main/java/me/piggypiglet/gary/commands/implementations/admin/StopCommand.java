package me.piggypiglet.gary.commands.implementations.admin;

import me.piggypiglet.gary.commands.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static me.piggypiglet.gary.file.implementations.json.types.Lang.STOP;
import static me.piggypiglet.gary.file.implementations.json.types.Lang.getMessage;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class StopCommand extends Command {
    public StopCommand() {
        super("stop");
        options.setPermission("stop");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage(getMessage(STOP)).queue();
        System.exit(0);
    }
}
