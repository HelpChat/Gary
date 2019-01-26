package me.piggypiglet.gary.commands.standalone;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.Roles;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RoleID extends Command {
    public RoleID() {
        super("roleid");
        options.setRole(Roles.TRUSTED).setDescription("Get a roles id from it's name.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length == 1) {
            e.getChannel().sendMessage(e.getGuild().getRolesByName(args[0].replace("\"", ""), true).get(0).getId()).queue();
        }
    }
}
