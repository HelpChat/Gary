package me.piggypiglet.gary.commands.standalone;

import me.piggypiglet.gary.core.framework.commands.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class PingCommand extends Command {
    public PingCommand() {
        super("ping");
        options.setDescription("Is gary online?");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage("pong, my connection latency is " + e.getJDA().getGatewayPing() + "ms").queue();
    }
}
