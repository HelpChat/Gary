package me.piggypiglet.gary.core.handlers.misc.services;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class RMSReactionHandler extends GEvent {
    public RMSReactionHandler() {
        super(EventsEnum.MESSAGE_REACTION_ADD);
    }

    @Override
    protected void execute(GenericEvent event) {
        GuildMessageReactionAddEvent e = (GuildMessageReactionAddEvent) event;

        if (e.getChannel().getName().equalsIgnoreCase("rate-my-server")) {
            Message message = e.getChannel().retrieveMessageById(e.getMessageId()).complete();

            if (message.getReactions().stream().anyMatch(d -> d.retrieveUsers().complete().contains(e.getUser()))) {
                e.getReaction().removeReaction(e.getUser()).queue();
            }
        }
    }
}
