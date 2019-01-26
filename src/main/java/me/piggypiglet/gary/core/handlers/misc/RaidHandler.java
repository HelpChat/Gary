package me.piggypiglet.gary.core.handlers.misc;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RaidHandler extends GEvent {
    public RaidHandler() {
        super(EventsEnum.MESSAGE_CREATE);
    }

    @Override
    protected void execute(Event event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;

//        if (e.getAuthor().getTimeCreated().getHour() <= 72) {
//            if (StringUtils.contains())
//        }
    }
}
