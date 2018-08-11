package me.piggypiglet.gary.core.handlers.chat;

import com.google.inject.Inject;
import me.piggypiglet.gary.ChatReaction;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.storage.mysql.tables.Stats;
import me.piggypiglet.gary.core.utils.message.StringUtils;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class StatHandler {
    @Inject private Stats stats;
    @Inject private ChatReaction chatReaction;

    public void check(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        TextChannel channel = e.getChannel();

        if (channel.getIdLong() == Constants.CR) {
            String word = chatReaction.getCurrentWord();

            if (message.equalsIgnoreCase(word)) {
                channel.sendMessage("Winner! " + e.getAuthor().getAsMention() + " Congratulations. The word was " + word).queue();
                stats.add("win", e.getMember().getUser());
                chatReaction.generateNewWord();

                return;
            }
        }

        if (StringUtils.hasWord(message, "o")) {
            stats.add("o", e.getMember().getUser());
        }

        if (StringUtils.hasWord(message, "bro")) {
            stats.add("bro", e.getMember().getUser());
        }
    }
}
