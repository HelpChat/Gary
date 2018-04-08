package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import me.piggypiglet.gary.chatreaction.ChatReaction;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.storage.Stats;
import me.piggypiglet.gary.core.utils.channel.MessageUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ChatHandler extends ListenerAdapter {
    @Inject private ChatReaction cr;
    @Inject private GFile files;
    @Inject private Stats stats;
    @Inject private MessageUtils messageUtils;

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            String message = e.getMessage().getContentRaw();

            if (messageUtils.hasWord(message, "o")) {
                stats.add("o", e.getMember().getUser());
            }
            if (messageUtils.hasWord(message, "bro")) {
                stats.add("bro", e.getMember().getUser());
            }

            if (e.getChannel().getIdLong() == Constants.CR) {
                String word = files.getItem("word-storage", "current-word");

                if (message.equalsIgnoreCase(word)) {
                    e.getChannel().sendMessage("Winner! " + e.getAuthor().getAsMention() + " Congratulations. The word was " + word).queue();
                    stats.add("win", e.getMember().getUser());
                    cr.generateNewWord();
                }
            }
        }
    }
}
