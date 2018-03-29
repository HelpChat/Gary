package me.piggypiglet.gary.core.tasks;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.channel.ChannelUtils;
import net.dv8tion.jda.core.JDA;

import java.util.TimerTask;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ChannelClearing extends TimerTask {
    @Inject private ChannelUtils cutil;
    private JDA jda;

    void setup(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void run() {
        cutil.purgeChannel(jda, Constants.REQUEST, Constants.REQUEST_MESSAGE);
        cutil.purgeChannel(jda, Constants.OFFER, Constants.OFFER_MESSAGE);
        cutil.purgeChannel(jda, Constants.RMS, Constants.RMS_MESSAGE);
        System.out.println("Request, offer and rms channels cleared!");
    }
}
