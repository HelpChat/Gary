package me.piggypiglet.gary.core.handlers.misc;

import com.google.inject.Singleton;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.objects.paginations.PaginationPage;
import me.piggypiglet.gary.core.objects.paginations.PaginationSet;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.HashMap;
import java.util.Map;

import static me.piggypiglet.gary.core.objects.enums.EventsEnum.MESSAGE_DELETE;
import static me.piggypiglet.gary.core.objects.enums.EventsEnum.MESSAGE_REACTION_ADD;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class PaginationHandler extends GEvent {
    private Map<String, PaginationSet> paginations;

    public PaginationHandler() {
        super(MESSAGE_REACTION_ADD, MESSAGE_DELETE);

        paginations = new HashMap<>();
    }

    public Map<String, PaginationSet> getPaginations() {
        return paginations;
    }

    @Override
    protected void execute(Event event) {
        switch (EventsEnum.fromEvent(event)) {
            case MESSAGE_REACTION_ADD:
                GuildMessageReactionAddEvent e = (GuildMessageReactionAddEvent) event;
                String messageId = e.getMessageId();

                if (paginations.containsKey(messageId)) {
                    MessageReaction.ReactionEmote reactionEmote = e.getReactionEmote();
                    PaginationPage newPage;

                    if (reactionEmote.getEmote() == null) {
                        newPage = paginations.get(messageId).getPage(reactionEmote.getName());
                    } else {
                        newPage = paginations.get(messageId).getPage(reactionEmote.getEmote());
                    }

                    if (newPage != null) {
                        Object newMessage = newPage.getMessage();
                        Message message = e.getChannel().getMessageById(messageId).complete();

                        if (newMessage instanceof String) {
                            message.editMessage((String) newMessage).queue();
                        } else if (newMessage instanceof MessageEmbed) {
                            message.editMessage((MessageEmbed) newMessage).override(true).queue();
                        }

                        e.getReaction().removeReaction(e.getUser()).queue();
                    }
                }

                break;

            case MESSAGE_DELETE:
                paginations.remove(((GuildMessageDeleteEvent) event).getMessageId());

                break;
        }
    }
}
