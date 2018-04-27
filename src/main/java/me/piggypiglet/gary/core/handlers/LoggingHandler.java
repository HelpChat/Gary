package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.LogType;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class LoggingHandler extends ListenerAdapter {
    @Inject private MessageUtils messageUtils;

    private List<Logger> loggers;

    public LoggingHandler() {
        loggers = new ArrayList<>();
    }

    public List<Logger> getLoggers() {
        return loggers;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        if (e.getGuild().getIdLong() == Constants.HELP_CHAT) {
            log(LogType.MEMBER_JOIN, e.getJDA(), e.getUser(), null, null, null, e.getGuild());
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
        if (e.getGuild().getIdLong() == Constants.HELP_CHAT) {
            log(LogType.MEMBER_LEAVE, e.getJDA(), e.getUser(), null, null, null, e.getGuild());
        }
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent e) {
        if (e.getGuild().getIdLong() == Constants.HELP_CHAT) {
            if (messageUtils.equalsIgnoreCase(e.getChannel().getName(), Constants.CHANNELS)) {
                log(LogType.MESSAGE_DELETE, e.getJDA(), null, e.getTextChannel(), null, e.getMessageIdLong(), e.getGuild());
            }
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent e) {
        if (e.getGuild().getIdLong() == Constants.HELP_CHAT) {
            if (messageUtils.equalsIgnoreCase(e.getChannel().getName(), Constants.CHANNELS)) {
                log(LogType.MESSAGE_EDIT, e.getJDA(), e.getAuthor(), e.getTextChannel(), e.getMessage(), e.getMessageIdLong(), e.getGuild());
            }
        }
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        if (e.getGuild().getIdLong() == Constants.HELP_CHAT) {
            log(LogType.VOICE_JOIN, e.getJDA(), e.getMember().getUser(), e.getChannelJoined(), null, null, e.getGuild());
        }
    }

    private void log(LogType type, JDA jda, User user, Channel channel, Message message, Long messageId, Guild guild) {
        loggers.forEach(logger -> {
            if (logger.getType() == type) {
                logger.log(jda, user, channel, message, messageId, guild);
            }
        });
    }
}
