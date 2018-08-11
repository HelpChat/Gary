package me.piggypiglet.gary.core.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.message.ChatReactionUtils;
import me.piggypiglet.gary.core.utils.message.StringUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.TimerTask;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class WordChanger extends TimerTask {
    @Inject private ChatReactionUtils crr;
    @Inject private GaryBot garyBot;

    private String word;
    private Message msg;

    public WordChanger() {
        if (word == null) {
            word = "";
        }
    }

    public void run() {
        if (msg != null) msg.delete().queue();

        TextChannel channel = garyBot.getJda().getTextChannelById(Constants.CR);
        word = crr.getRandomWord();
        String scrambled = crr.scrambleWord(word);
        MessageEmbed message = new EmbedBuilder()
                .setTitle("Word Update")
                .setDescription(StringUtils.bigLetters(scrambled))
                .setColor(Constants.ROLE_COLOR)
                .setFooter("Gary v" + Constants.VERSION, Constants.AVATAR)
                .build();

        msg = channel.sendMessage(message).complete();
    }

    public Message getCurrentMessage() {
        return msg;
    }

    public String getCurrentWord() {
        return word;
    }
}
