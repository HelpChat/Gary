package me.piggypiglet.gary.core.handlers.misc;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GiveawayHandler extends GEvent {
    public GiveawayHandler() {
        super(EventsEnum.MESSAGE_REACTION_ADD, EventsEnum.MESSAGE_REACTION_REMOVE);
    }

    @Override
    protected void execute(Event event) {
        GenericGuildMessageReactionEvent e = (GenericGuildMessageReactionEvent) event;

        if (!e.getUser().isBot() && e.getReactionEmote().getName().equalsIgnoreCase(Constants.THUMBSUP) && e.getMessageIdLong() == Constants.GIVEAWAY_MESSAGE) {
            Guild guild = e.getGuild();
            Role role = guild.getRoleById(Constants.GIVEAWAY);

            switch (EventsEnum.fromEvent(event)) {
                case MESSAGE_REACTION_ADD:
                    guild.getController().addRolesToMember(e.getMember(), role).queue();
                    break;

                case MESSAGE_REACTION_REMOVE:
                    guild.getController().removeRolesFromMember(e.getMember(), role).queue();
                    break;
            }
        }
    }
}
