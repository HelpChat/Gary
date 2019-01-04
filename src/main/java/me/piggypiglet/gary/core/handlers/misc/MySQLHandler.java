package me.piggypiglet.gary.core.handlers.misc;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.utils.mysql.MessageUtils;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;

import java.util.Arrays;

import static me.piggypiglet.gary.core.objects.enums.EventsEnum.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MySQLHandler extends GEvent {
    public MySQLHandler() {
        super(MESSAGE_CREATE, MESSAGE_EDIT, MESSAGE_DELETE);
    }

    @Override
    protected void execute(Event event) {
        GenericGuildMessageEvent e = (GenericGuildMessageEvent) event;

        switch (EventsEnum.fromEvent(event)) {
            case MESSAGE_CREATE:
                if (!e.getChannel().getMessageById(e.getMessageId()).complete().getAuthor().isBot() && StringUtils.contains(e.getChannel().getId(), Arrays.asList(Constants.CHANNELS))) {
                    MessageUtils.addMessage(((GuildMessageReceivedEvent) event).getMessage());
                }
                break;

            case MESSAGE_EDIT:
                if (!MessageUtils.getAuthor(e.getMessageIdLong()).isBot()) {
                    MessageUtils.editMessage(((GuildMessageUpdateEvent) event).getMessage());
                }
                break;

            case MESSAGE_DELETE:
                if (!MessageUtils.getAuthor(e.getMessageIdLong()).isBot()) {
                    // sleep before deleting from mysql so logger can grab the message content
                    instance.sleep(100);
                    MessageUtils.deleteMessage(((GuildMessageDeleteEvent) event).getMessageIdLong());
                }
                break;
        }
    }
}
