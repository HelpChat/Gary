package me.piggypiglet.gary.core.objects.enums;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public enum EventsEnum {
    MEMBER_JOIN(GuildMemberJoinEvent.class),
    MEMBER_LEAVE(GuildMemberLeaveEvent.class),
    MEMBER_BANNED(GuildBanEvent.class),
    MEMBER_UNBANNED(GuildUnbanEvent.class),
    MESSAGE_CREATE(GuildMessageReceivedEvent.class),
    MESSAGE_EDIT(GuildMessageUpdateEvent.class),
    MESSAGE_DELETE(GuildMessageDeleteEvent.class),
    MESSAGE_BULK_DELETE(MessageBulkDeleteEvent.class),
    MESSAGE_REACTION_ADD(GuildMessageReactionAddEvent.class),
    MESSAGE_REACTION_REMOVE(GuildMessageReactionRemoveEvent.class),
    TEXT_CHANNEL_CREATE(TextChannelCreateEvent.class),
    TEXT_CHANNEL_DELETE(TextChannelDeleteEvent.class),
    VOICE_CHANNEL_CREATE(VoiceChannelCreateEvent.class),
    VOICE_CHANNEL_DELETE(VoiceChannelDeleteEvent.class),
    ROLE_CREATE(RoleCreateEvent.class),
    ROLE_DELETE(RoleDeleteEvent.class),
    ROLE_UPDATE(RoleUpdateColorEvent.class),
    ROLE_GIVE(GuildMemberRoleAddEvent.class),
    ROLE_REMOVE(GuildMemberRoleRemoveEvent.class),
    NICKNAME_CHANGE(GuildMemberUpdateNicknameEvent.class),
    VOICE_JOIN(GuildVoiceJoinEvent.class),
    VOICE_LEAVE(GuildVoiceLeaveEvent.class),
    VOICE_MOVE(GuildVoiceMoveEvent.class),
    UNKNOWN(null);

    private final Class<? extends GenericEvent> event;

    EventsEnum(Class<? extends GenericEvent> c) {
        this.event = c;
    }

    public static EventsEnum fromEvent(GenericEvent e) {
        for (EventsEnum type : values()) {
            if (type.event == e.getClass()) {
                return type;
            }
        }

        return UNKNOWN;
    }

    public static boolean contains(GenericEvent e) {
        for (EventsEnum type : values()) {
            if (type.event == e.getClass()) {
                return true;
            }
        }

        return false;
    }
}
