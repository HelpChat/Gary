package me.piggypiglet.gary.aprilfirst.handlers;

import com.google.inject.Inject;
import me.piggypiglet.gary.aprilfirst.ChatReaction;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.GFile;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class ChatHandler extends ListenerAdapter {
    @Inject private ChatReaction cr;
    @Inject private GFile files;

    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            if (e.getChannel().getIdLong() == Constants.CR) {
                if (e.getMessage().getContentRaw().equalsIgnoreCase(files.getItem("word-storage", "current-word"))) {
                    e.getChannel().sendMessage("Winner! " + e.getAuthor().getAsMention() + " Congratulations. The word was " + files.getItem("word-storage", "current-word")).queue();
                    cr.generateNewWord();
                }
            }
        }
    }
}
