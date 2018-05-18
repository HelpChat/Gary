package me.piggypiglet.gary.core.handlers;

import com.google.inject.Singleton;
import me.piggypiglet.gary.core.objects.pagination.PaginationPage;
import me.piggypiglet.gary.core.objects.pagination.PaginationSet;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class PaginationHandler extends ListenerAdapter {
    private Map<String, PaginationSet> paginations;

    public PaginationHandler() {
        paginations = new HashMap<>();
    }

    public Map<String, PaginationSet> getPaginations() {
        return paginations;
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        if (!e.getUser().isBot()) {
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
                        message.editMessage((MessageEmbed) message).queue();
                    }

                    e.getReaction().removeReaction(e.getUser()).queue();
                }
            }
        }
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent e) {
        paginations.remove(e.getMessageId());
    }
}
