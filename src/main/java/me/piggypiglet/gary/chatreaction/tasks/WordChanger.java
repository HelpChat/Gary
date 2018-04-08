package me.piggypiglet.gary.chatreaction.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.chatreaction.utils.CRRandom;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.objects.Version;
import me.piggypiglet.gary.core.utils.channel.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class WordChanger extends TimerTask {
    @Inject private CRRandom crr;
    @Inject private MessageUtils mutil;
    @Inject private GFile files;
    @Inject private Version version;
    private JDA jda;

    public void setup(JDA jda) {
        this.jda = jda;
    }

    public void run() {
        TextChannel channel = jda.getTextChannelById(Constants.CR);
        String word = crr.getRandomWord();
        String scrambled = crr.scrambleWord(word);
        MessageEmbed message = new EmbedBuilder()
                .setTitle("Word Update")
                .setDescription(mutil.bigLetters(scrambled))
                .setFooter("Gary v" + version.getVersion(), "https://cdn.discordapp.com/avatars/332142935380459520/2d2b0a78ec3ab461f23721a51a292a3e.png")
                .build();

//        channel.getManager().setTopic("Scramble >> " + mutil.bigLetters(scrambled)).queue();
        channel.sendMessage(message).complete().delete().queueAfter(5, TimeUnit.MINUTES);
        files.setWord(word);
    }
}
