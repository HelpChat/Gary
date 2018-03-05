package me.piggypiglet.gary.core.tasks;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.util.ChannelUtil;
import net.dv8tion.jda.core.JDA;

import java.util.TimerTask;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ChannelClearing extends TimerTask {
    @Inject private ChannelUtil cutil;
    private JDA jda;

    public void setup(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void run() {
        cutil.purgeChannel(jda, Constants.REQUEST, Constants.REQUEST_MESSAGE);
        cutil.purgeChannel(jda, Constants.OFFER, Constants.OFFER_MESSAGE);
        System.out.println("Request and Offer channels cleared!");
    }
}
