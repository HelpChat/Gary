package me.piggypiglet.gary.chatreaction.handlers;

import com.google.inject.Inject;
import me.piggypiglet.gary.chatreaction.ChatReaction;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.storage.Stats;
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

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            if (e.getChannel().getIdLong() == Constants.CR) {
                if (e.getMessage().getContentRaw().equalsIgnoreCase(files.getItem("word-storage", "current-word"))) {
                    e.getChannel().sendMessage("Winner! " + e.getAuthor().getAsMention() + " Congratulations. The word was " + files.getItem("word-storage", "current-word")).queue();
                    stats.addWin(e.getMember().getUser());
                    cr.generateNewWord();
                }
            }
        }
    }
}
