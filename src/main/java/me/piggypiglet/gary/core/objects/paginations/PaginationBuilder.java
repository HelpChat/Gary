package me.piggypiglet.gary.core.objects.paginations;

import com.google.inject.Inject;
import lombok.Getter;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PaginationBuilder {
    @Inject private PaginationHandler paginationHandler;

    @Getter private List<PaginationPage> pages;

    public PaginationBuilder() {
        pages = new ArrayList<>();
    }

    public void addPages(PaginationPage... pages) {
        this.pages.addAll(Arrays.asList(pages));
    }

    public void build(TextChannel channel) {
        if (!pages.isEmpty()) {
            List<Object> emotes = new ArrayList<>();
            Object message_ = pages.get(0).getMessage();
            Message message = null;
            PaginationSet paginationSet = new PaginationSet().addPages(pages);

            pages.stream().map(PaginationPage::getEmote).forEach(emotes::add);

            if (message_ instanceof String) {
                message = channel.sendMessage((String) message_).complete();
            } else if (message_ instanceof MessageEmbed) {
                message = channel.sendMessage((MessageEmbed) message_).complete();
            }

            Message finalMessage = message;
            emotes.forEach(emote -> {
                if (emote instanceof String) {
                    Objects.requireNonNull(finalMessage).addReaction((String) emote).queue();
                } else if (emote instanceof Emote) {
                    Objects.requireNonNull(finalMessage).addReaction((Emote) emote).queue();
                }
            });

            paginationHandler.getPaginations().put(Objects.requireNonNull(finalMessage).getId(), paginationSet);

            pages = null;
        }
    }
}