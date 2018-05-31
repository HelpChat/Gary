package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import me.piggypiglet.gary.ChatReaction;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.storage.mysql.tables.Messages;
import me.piggypiglet.gary.core.storage.mysql.tables.Stats;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import me.piggypiglet.gary.core.utils.message.RMSUtils;
import me.piggypiglet.gary.core.utils.message.RequestUtils;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ChatHandler extends ListenerAdapter {
    @Inject private ChatReaction cr;
    @Inject private Stats stats;
    @Inject private Messages messages;
    @Inject private MessageUtils messageUtils;
    @Inject private RequestUtils requestUtils;
    @Inject private RMSUtils rmsUtils;

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
                String word = cr.getCurrentWord();

                if (message.equalsIgnoreCase(word)) {
                    e.getChannel().sendMessage("Winner! " + e.getAuthor().getAsMention() + " Congratulations. The word was " + word).queue();
                    stats.add("win", e.getMember().getUser());
                    cr.generateNewWord();
                }
            }

            checkMessage(e);
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent e) {
        if (!e.getAuthor().isBot()) {
            checkMessage(e);
        }
    }

    private void checkMessage(GenericMessageEvent e) {
        long channelId = e.getChannel().getIdLong();

        if (channelId == Constants.REQUEST_FREE || channelId == Constants.REQUEST_PAID) {
            requestUtils.checkMessage(e, channelId);
        }
        if (channelId == Constants.RMS) {
            rmsUtils.createMessage(e);
        }
    }
}
