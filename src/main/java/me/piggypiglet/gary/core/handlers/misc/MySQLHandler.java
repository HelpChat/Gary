package me.piggypiglet.gary.core.handlers.misc;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.utils.mysql.MessageUtils;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;

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
        switch (EventsEnum.fromEvent(event)) {
            case MESSAGE_CREATE:
                MessageUtils.addMessage(((GuildMessageReceivedEvent) event).getMessage());
                break;

            case MESSAGE_EDIT:
                MessageUtils.editMessage(((GuildMessageUpdateEvent) event).getMessage());
                break;

            case MESSAGE_DELETE:
                // sleep before deleting from mysql so logger can grab the message content
                instance.sleep(100);
                MessageUtils.deleteMessage(((GuildMessageDeleteEvent) event).getMessageIdLong());
                break;
        }
    }
}
