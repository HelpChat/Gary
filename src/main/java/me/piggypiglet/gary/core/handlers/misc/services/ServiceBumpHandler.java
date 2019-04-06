package me.piggypiglet.gary.core.handlers.misc.services;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.utils.http.HasteUtils;
import me.piggypiglet.gary.core.utils.mysql.BumpUtils;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static me.piggypiglet.gary.core.objects.enums.EventsEnum.MESSAGE_CREATE;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServiceBumpHandler extends GEvent {
    public ServiceBumpHandler() {
        super(MESSAGE_CREATE);
    }

    @Override
    protected void execute(GenericEvent event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;

        if (StringUtils.equalsIgnoreCase(e.getChannel().getName(), "offer-services", "request-free", "request-paid")) {
            String message = e.getMessage().getContentRaw();
            User user = e.getAuthor();

            if (BumpUtils.contains(user.getIdLong(), message)) {
                e.getChannel().sendMessage(
                        e.getAuthor().getAsMention() + "\nI've removed your request as it is too similar to one of your other requests.\n" + HasteUtils.haste(message)
                ).queue();
            } else {
                BumpUtils.create(user.getIdLong(), message);
            }
        }
    }
}
