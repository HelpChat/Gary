package me.piggypiglet.gary.core.handlers.misc;

import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.concurrent.ThreadLocalRandom;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class InsultHandler {
    public void check(GuildMessageReactionAddEvent e) {
        Message message = e.getChannel().getMessageById(e.getMessageIdLong()).complete();
        String footer = message.getEmbeds().get(0).getFooter().getIconUrl().split("/")[4];
        boolean emote = e.getReactionEmote().getIdLong() == 424527593342107649L;

        if (e.getChannel().getIdLong() == Constants.RMS && e.getUser().getId().equals(footer)) {
            e.getReaction().removeReaction(e.getUser()).queue();

            if (emote) {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                String shame = String.format(Constants.SHAMES[random.nextInt(4)], e.getUser().getAsMention());

                e.getGuild().getTextChannelById(Constants.OTHER).sendMessage(shame).queue();
            }
        }
    }
}
