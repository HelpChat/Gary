package me.piggypiglet.gary.commands;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.Roles;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Test extends Command {
    public Test() {
        super("type");
        options.setDescription("please don't run").setRole(Roles.ADMIN);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendTyping().queue();
    }
}
