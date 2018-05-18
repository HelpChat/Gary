package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.storage.mysql.tables.Messages;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class LoggingHandler implements EventListener {
    @Inject private MessageUtils messageUtils;
    @Inject private Messages messages;

    private List<Logger> loggers;
    private boolean wasBan;

    public LoggingHandler() {
        loggers = new ArrayList<>();
        wasBan = false;
    }

    public List<Logger> getLoggers() {
        return loggers;
    }

    @Override
    public void onEvent(Event event) {
        switch (EventsEnum.fromEvent(event.getClass())) {
            case MEMBER_JOIN:
                GuildMemberJoinEvent e = (GuildMemberJoinEvent) event;

                log(EventsEnum.MEMBER_JOIN, e.getJDA(), e.getGuild(), e.getUser());
                break;

            case MEMBER_LEAVE:
                if (!wasBan) {
                    GuildMemberLeaveEvent e2 = (GuildMemberLeaveEvent) event;

                    log(EventsEnum.MEMBER_LEAVE, e2.getJDA(), e2.getGuild(), e2.getUser());
                } else {
                    wasBan = false;
                }
                break;

            case MEMBER_BANNED:
                wasBan = true;
                GuildBanEvent e3 = (GuildBanEvent) event;

                log(EventsEnum.MEMBER_BANNED, e3.getJDA(), e3.getGuild(), e3.getUser());
                break;

            case MESSAGE_CREATE:
                GuildMessageReceivedEvent e4 = (GuildMessageReceivedEvent) event;

                if (!e4.getAuthor().isBot() && (messageUtils.equalsIgnoreCase(e4.getChannel().getId(), Constants.CHANNELS))) {
                    messages.addMessage(e4.getMessage());
                }
                break;

            case MESSAGE_EDIT:
                GuildMessageUpdateEvent e5 = (GuildMessageUpdateEvent) event;

                if (messageUtils.equalsIgnoreCase(e5.getChannel().getId(), Constants.CHANNELS)) {
                    log(EventsEnum.MESSAGE_EDIT, e5.getJDA(), e5.getGuild(), e5.getAuthor(), e5.getChannel(), e5.getMessage());
                }
                break;

            case MESSAGE_DELETE:
                GuildMessageDeleteEvent e6 = (GuildMessageDeleteEvent) event;

                if ((messageUtils.equalsIgnoreCase(e6.getChannel().getId(), Constants.CHANNELS))) {
                    log(EventsEnum.MESSAGE_DELETE, e6.getJDA(), e6.getGuild(), e6.getChannel(), e6.getMessageIdLong());
                }
                break;

            case MESSAGE_BULK_DELETE:
                MessageBulkDeleteEvent e7 = (MessageBulkDeleteEvent) event;

                if ((messageUtils.equalsIgnoreCase(e7.getChannel().getId(), Constants.CHANNELS))) {
                    log(EventsEnum.MESSAGE_BULK_DELETE, e7.getJDA(), e7.getGuild(), e7.getChannel(), e7.getMessageIds());
                }
                break;

            case VOICE_JOIN:
                GuildVoiceJoinEvent e8 = (GuildVoiceJoinEvent) event;

                log(EventsEnum.VOICE_JOIN, e8.getJDA(), e8.getGuild(), e8.getMember().getUser(), e8.getChannelJoined());
                break;
        }
    }

    private void log(EventsEnum type, JDA jda, Guild guild, Object... other) {
        loggers.forEach(logger -> {
            if (logger.getType() == type) {
                logger.log(jda, guild, other);
            }
        });
    }

}
