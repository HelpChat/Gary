package me.piggypiglet.gary.core.handlers.misc.services;

import me.piggypiglet.gary.core.utils.http.HasteUtils;
import me.piggypiglet.gary.core.utils.mysql.BumpUtils;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServiceBumpHandler {
    public void execute(GenericGuildMessageEvent event) {
        if (event instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;
            User user = e.getAuthor();

            if (!user.isBot() && StringUtils.equalsIgnoreCase(e.getChannel().getName(), "offer-services", "request-free", "request-paid")) {
                String message = e.getMessage().getContentRaw();

                if (BumpUtils.contains(user.getIdLong(), message)) {
                    e.getMessage().delete().queue();

                    e.getChannel().sendMessage(
                            e.getAuthor().getAsMention() + "\nI've removed your request as it is too similar to one of your other requests.\n" + HasteUtils.haste(message)
                    ).queue(s -> s.delete().queueAfter(30, TimeUnit.SECONDS));
                } else {
                    BumpUtils.create(user.getIdLong(), message);
                }
            }
        }
    }
}
