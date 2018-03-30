package me.piggypiglet.gary.aprilfirst.tasks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.aprilfirst.utils.CRRandom;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.utils.channel.MessageUtils;
import net.dv8tion.jda.core.JDA;

import java.util.TimerTask;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class WordChanger extends TimerTask {
    @Inject private CRRandom crr;
    @Inject private MessageUtils mutil;
    @Inject private GFile files;
    private JDA jda;

    public void setup(JDA jda) {
        this.jda = jda;
    }

    public void run() {
        String word = crr.getRandomWord();
        jda.getGuildById(Constants.HELP_CHAT).getTextChannelById(Constants.CR).getManager().setTopic("Scramble >> " + mutil.bigLetters(crr.scrambleWord(word))).queue();
        files.setWord(word);
    }
}
