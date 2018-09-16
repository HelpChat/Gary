package me.piggypiglet.gary.core.utils.discord;

import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.concurrent.atomic.AtomicReference;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class EventUtils {
    private static Event pullEvent(EventsEnum event, JDA jda) {
        final AtomicReference<Event> pulledEvent = new AtomicReference<>();

        EventListener listener = l -> {
            if (EventsEnum.fromEvent(l) == event) {
                pulledEvent.set(l);
            }
        };

        jda.addEventListener(listener);

        while (pulledEvent.get() == null) try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jda.removeEventListener(listener);

        return pulledEvent.get();
    }

    public static Message pullMessage(TextChannel channel, User user) {
        GuildMessageReceivedEvent event = (GuildMessageReceivedEvent) pullEvent(EventsEnum.MESSAGE_CREATE, channel.getJDA());

        while (event.getChannel() != channel && event.getAuthor() != user) {
            event = (GuildMessageReceivedEvent) pullEvent(EventsEnum.MESSAGE_CREATE, channel.getJDA());
        }

        return event.getMessage();
    }
}