package me.piggypiglet.gary.commands.placeholderapi;

import me.piggypiglet.gary.core.framework.commands.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class PapiCommand extends Command {
    public PapiCommand() {
        super("papi");
        options.setDescription("Papi command");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage("Gary's placeholderapi system is retired, please use <@510750938672136193> with `-papi`.").queue();
    }
}
