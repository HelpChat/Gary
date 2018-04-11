package me.piggypiglet.gary.core.tasks;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.utils.misc.ChannelUtils;
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
        int limit = 9999999;
//        cutil.purgeChannel(cutil.getTextChannel(jda, Constants.REQUEST), Constants.REQUEST_MESSAGE, limit,false);
//        cutil.purgeChannel(cutil.getTextChannel(jda, Constants.OFFER), Constants.OFFER_MESSAGE, limit,false);
//        cutil.purgeChannel(cutil.getTextChannel(jda, Constants.RMS), Constants.RMS_MESSAGE, limit,false);
        System.out.println("Request, offer and rms channels cleared!");
    }
}
