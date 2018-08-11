package me.piggypiglet.gary.core.handlers.misc;

import me.piggypiglet.gary.core.objects.pagination.PaginationPage;
import me.piggypiglet.gary.core.objects.pagination.PaginationSet;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.HashMap;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PaginationHandler {
    private Map<String, PaginationSet> paginations;

    public PaginationHandler() {
        paginations = new HashMap<>();
    }

    public Map<String, PaginationSet> getPaginations() {
        return paginations;
    }

    public void add(GuildMessageReactionAddEvent e) {
        String messageId = e.getMessageId();

        if (paginations.containsKey(messageId)) {
            PaginationPage newPage;

            if (e.getReaction().getReactionEmote().getEmote() == null) {
                newPage = paginations.get(messageId).getPage(e.getReactionEmote().getName());
            } else {
                newPage = paginations.get(messageId).getPage(e.getReactionEmote().getEmote());
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
    }

    public void remove(GuildMessageDeleteEvent e) {
        paginations.remove(e.getMessageId());
    }

    public void clearPaginations() {
        paginations.clear();
    }
}
