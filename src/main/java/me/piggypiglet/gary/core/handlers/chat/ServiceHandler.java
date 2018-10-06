package me.piggypiglet.gary.core.handlers.chat;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServiceHandler extends GEvent {
    public ServiceHandler() {
        super(EventsEnum.MESSAGE_CREATE, EventsEnum.MESSAGE_EDIT);
    }

    @Override
    protected void execute(Event event) {
        GenericGuildMessageEvent e = (GenericGuildMessageEvent) event;

        switch (e.getChannel().getId()) {
            case Constants.REQUEST_FREE:

                break;

//            case Constants.REQUEST_PAID:
//                break;
//
//            case Constants.RMS:
//                break;
        }
    }
}
