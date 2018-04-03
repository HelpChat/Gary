package me.piggypiglet.gary.commands.admin;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class BanCheck extends Command {

    @Inject private RoleUtils rutil;

    public BanCheck() {
        super("?bancheck");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (rutil.isStaff(e.getMember())) {
            if (args.length != 1) {
                return;
            }

            MessageChannel channel = e.getChannel();
            int length = args[0].length();
            if (length >= 5) {
                if (Character.toString(args[0].charAt(length - 5)).equalsIgnoreCase("#")) {
                    List<Guild.Ban> bans = e.getGuild().getBanList().complete();
                    Map<String, String> users = new HashMap<>();
                    bans.forEach(ban -> users.put(ban.getUser().getName() + "#" + ban.getUser().getDiscriminator(), ban.getReason()));

                    String banned = "";
                    for (String s : users.keySet()) {
                        banned = s.equalsIgnoreCase(args[0]) ? "banned." : "not banned.";
                    }
                    channel.sendMessage(args[0] + " is " + banned).queue();
                } else {
                    channel.sendMessage("Invalid name/discriminator").queue();
                }
            } else if (length == 0) {
                channel.sendMessage("Incorrect usage of `?bancheck`. Use like this: `?bancheck username#discriminator`").queue();
            } else {
                channel.sendMessage("Invalid name/discriminator").queue();
            }
        }
    }

}
