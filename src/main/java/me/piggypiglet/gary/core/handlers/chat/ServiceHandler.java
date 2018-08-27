package me.piggypiglet.gary.core.handlers.chat;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.message.RMSUtils;
import me.piggypiglet.gary.core.utils.message.RequestUtils;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServiceHandler {
    @Inject private RequestUtils requestUtils;
    @Inject private RMSUtils rmsUtils;

    public void check(GenericGuildMessageEvent e) {
        long channelId = e.getChannel().getIdLong();

        if (channelId == Constants.REQUEST_FREE || channelId == Constants.REQUEST_PAID) {
            requestUtils.checkMessage(e, channelId);
        }

        if (channelId == Constants.RMS) {
            rmsUtils.createMessage(e);
        }
    }
}
