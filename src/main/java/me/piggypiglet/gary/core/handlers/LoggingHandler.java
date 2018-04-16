package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import me.piggypiglet.gary.core.utils.misc.LogUtils;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class LoggingHandler extends ListenerAdapter {
    @Inject private LogUtils logUtils;
    @Inject private MessageUtils messageUtils;

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        logUtils.memberJoin(e);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
        // TODO: is ban?
        logUtils.memberLeave(e);
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent e) {
        if (messageUtils.equalsIgnoreCase(e.getChannel().getName(), Constants.CHANNELS)) {
            logUtils.messageDelete(e);
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent e) {
        if (messageUtils.equalsIgnoreCase(e.getChannel().getName(), Constants.CHANNELS)) {
            logUtils.messageEdit(e);
        }
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        logUtils.voiceJoin(e);
    }
}
