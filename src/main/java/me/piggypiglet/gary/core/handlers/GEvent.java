package me.piggypiglet.gary.core.handlers;

import lombok.Getter;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.events.Event;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class GEvent {
    @Getter private EventsEnum[] events;

    protected GEvent(EventsEnum... events) {
        this.events = events;
    }

    protected abstract void execute(Event event);
}
