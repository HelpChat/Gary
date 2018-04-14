package me.piggypiglet.gary.core.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.message.CRRandomUtils;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
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
    @Inject private CRRandomUtils crr;
    @Inject private MessageUtils mutil;
    private String word;
    private Message msg;
    private JDA jda;

    public WordChanger() {
        if (word == null) {
            word = "";
        }
    }

    void setup(JDA jda) {
        this.jda = jda;
    }

    public void run() {
        if (msg != null) msg.delete().queue();

        TextChannel channel = jda.getTextChannelById(Constants.CR);
        word = crr.getRandomWord();
        String scrambled = crr.scrambleWord(word);
        MessageEmbed message = new EmbedBuilder()
                .setTitle("Word Update")
                .setDescription(mutil.bigLetters(scrambled))
                .setColor(Constants.ROLE_COLOR)
                .setFooter("Gary v" + Constants.VERSION, "https://cdn.discordapp.com/avatars/332142935380459520/2d2b0a78ec3ab461f23721a51a292a3e.png")
                .build();

//        channel.getManager().setTopic("Scramble >> " + mutil.bigLetters(scrambled)).queue();
        msg = channel.sendMessage(message).complete();
    }

    public String getCurrentWord() {
        return word;
    }
}
