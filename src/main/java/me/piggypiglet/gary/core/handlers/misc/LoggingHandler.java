package me.piggypiglet.gary.core.handlers.misc;

import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.framework.logging.Logger;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.utils.mysql.MessageUtils;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;

import java.util.ArrayList;
import java.util.List;

import static me.piggypiglet.gary.core.objects.enums.EventsEnum.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class LoggingHandler extends GEvent {
    @Getter private final List<Logger> loggers = new ArrayList<>();
    private boolean wasBan = false;

    public LoggingHandler() {
        super(MEMBER_JOIN, MEMBER_LEAVE, MEMBER_BANNED, MESSAGE_EDIT, MESSAGE_DELETE, MESSAGE_BULK_DELETE, VOICE_JOIN);
    }

    @Override
    protected void execute(GenericEvent event) {
        switch (EventsEnum.fromEvent(event)) {
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

            case MESSAGE_EDIT:
                GuildMessageUpdateEvent e5 = (GuildMessageUpdateEvent) event;

                if (!e5.getAuthor().isBot() && StringUtils.equalsIgnoreCase(e5.getChannel().getName(), Constants.CHANNELS)) {
                    log(EventsEnum.MESSAGE_EDIT, e5.getJDA(), e5.getGuild(), e5.getAuthor(), e5.getChannel(), e5.getMessage());
                }

                break;

            case MESSAGE_DELETE:
                GuildMessageDeleteEvent e6 = (GuildMessageDeleteEvent) event;

                if ((StringUtils.equalsIgnoreCase(e6.getChannel().getName(), Constants.CHANNELS))) {
                    log(EventsEnum.MESSAGE_DELETE, e6.getJDA(), e6.getGuild(), MessageUtils.getAuthor(e6.getMessageIdLong()), e6.getChannel(), e6.getMessageIdLong(), MessageUtils.getMessage(e6.getMessageIdLong()));
                }

                break;

            case MESSAGE_BULK_DELETE:
                MessageBulkDeleteEvent e7 = (MessageBulkDeleteEvent) event;

                if ((StringUtils.equalsIgnoreCase(e7.getChannel().getName(), Constants.CHANNELS))) {
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
