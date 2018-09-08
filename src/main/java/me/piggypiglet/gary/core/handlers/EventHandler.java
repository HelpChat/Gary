package me.piggypiglet.gary.core.handlers;

import com.google.inject.Singleton;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class EventHandler implements EventListener {
    private List<GEvent> events;

    public EventHandler() {
        events = new ArrayList<>();
    }

    public List<GEvent> getEvents() {
        return events;
    }

    @Override
    public void onEvent(Event event) {
        if (EventsEnum.contains(event)) {
            events.forEach(e -> {
                if (Arrays.asList(e.getEvents()).contains(EventsEnum.fromEvent(event))) {
                    e.execute(event);
                }
            });
        }
    }
}
