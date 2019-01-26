package me.piggypiglet.gary.commands.standalone;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.Roles;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PurgeCommand extends Command {
    public PurgeCommand() {
        super("purge");
        options.setRole(Roles.ADMIN).setDescription("Purge an amount of messages in the current channel.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        TextChannel channel = e.getChannel();

        if (args.length == 1) {
            int amount;

            try {
                amount = Integer.parseInt(args[0]);
            } catch (Exception ignored) {
                channel.sendMessage("Please supply a number, not `" + args[0] + "`.").queue();
                return;
            }

            channel.purgeMessages(channel.getHistory().retrievePast(amount + 1).complete());
            channel.sendMessage("Purged.").queue(s -> s.delete().queueAfter(10, TimeUnit.SECONDS));
        } else {
            channel.sendMessage("Please supply an amount of messages.").queue();
        }
    }
}
