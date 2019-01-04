package me.piggypiglet.gary.core.handlers;

import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.objects.tasks.Task;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class EventHandler implements EventListener {
    @Getter private List<GEvent> events;

    public EventHandler() {
        events = new ArrayList<>();
    }

    @Override
    public void onEvent(Event event) {
        if (EventsEnum.contains(event)) {
            events.forEach(e -> {
                if (Arrays.asList(e.getEvents()).contains(EventsEnum.fromEvent(event))) {
                    Task.async((r) -> e.run(event, r));
                }
            });
        }
    }
}
