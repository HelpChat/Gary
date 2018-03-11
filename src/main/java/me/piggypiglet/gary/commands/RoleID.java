package me.piggypiglet.gary.commands;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RoleID extends Command {

    @Inject private RoleUtils rutil;

    public RoleID() {
        super("?roleid");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (args.length == 1) {
            Role role = rutil.getRole(e.getGuild(), args[0]);
            MessageChannel channel = e.getChannel();
            channel.sendMessage(role == null ? "Role doesn't exist or isn't found!" : role.getId()).queue();
        }
    }

}
