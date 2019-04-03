package me.piggypiglet.gary.core.handlers.misc.services;

import me.piggypiglet.gary.core.handlers.GEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;

import static me.piggypiglet.gary.core.objects.enums.EventsEnum.MESSAGE_CREATE;
import static me.piggypiglet.gary.core.objects.enums.EventsEnum.MESSAGE_DELETE;
// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServiceBumpHandler extends GEvent {
    public ServiceBumpHandler() {
        super(MESSAGE_CREATE, MESSAGE_DELETE);
    }

    @Override
    protected void execute(GenericEvent event) {
        GenericGuildMessageEvent ev = (GenericGuildMessageEvent) event;
//
//        if (StringUtils.equalsIgnoreCase(ev.getChannel().getName(), "offer-services", "request-free", "request-paid")) {
//            switch (EventsEnum.fromEvent(event)) {
//                case MESSAGE_CREATE:
//                    GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;
//
//                    if (BumpUtils.contains(e.getAuthor().getIdLong(), e.getMessage().getContentRaw(), p -> FuzzySearch.ratio(p, BumpUtils.get()))
//
//                    break;
//
//                case MESSAGE_DELETE:
//                    GuildMessageDeleteEvent e2 = (GuildMessageDeleteEvent) event;
//
//                    if (BumpUtils.c)
//
//                    break;
//            }
//        }
    }
}
