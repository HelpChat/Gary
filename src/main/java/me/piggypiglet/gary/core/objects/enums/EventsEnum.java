package me.piggypiglet.gary.core.objects.enums;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public enum EventsEnum {
    MEMBER_JOIN(GuildMemberJoinEvent.class),
    MEMBER_LEAVE(GuildMemberLeaveEvent.class),
    MEMBER_BANNED(GuildBanEvent.class),
    MESSAGE_CREATE(GuildMessageReceivedEvent.class),
    MESSAGE_EDIT(GuildMessageUpdateEvent.class),
    MESSAGE_DELETE(GuildMessageDeleteEvent.class),
    MESSAGE_BULK_DELETE(MessageBulkDeleteEvent.class),
    VOICE_JOIN(GuildVoiceJoinEvent.class),
    REACTION_ADD(GuildMessageReactionAddEvent.class),
    REACTION_REMOVE(GuildMessageReactionRemoveEvent.class),
    UNKNOWN(null);

    private final Class<? extends Event> event;

    EventsEnum(Class<? extends Event> c) {
        this.event = c;
    }

    public static EventsEnum fromEvent(Class<? extends Event> e) {
        for (EventsEnum type : values()) {
            if (type.event == e) {
                return type;
            }
        }

        return UNKNOWN;
    }
}
