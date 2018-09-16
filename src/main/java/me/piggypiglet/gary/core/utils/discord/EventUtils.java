package me.piggypiglet.gary.core.utils.discord;

import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
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

    public static Future<Message> pullMessage(TextChannel channel, User user) {
        CompletableFuture<Message> future = new CompletableFuture<>();
        final AtomicReference<GuildMessageReceivedEvent> e = new AtomicReference<>((GuildMessageReceivedEvent) pullEvent(EventsEnum.MESSAGE_CREATE, channel.getJDA()));

            while (e.get().getChannel() != channel && e.get().getAuthor() != user) {
                e.set((GuildMessageReceivedEvent) pullEvent(EventsEnum.MESSAGE_CREATE, channel.getJDA()));
            }

            future.complete(e.get().getMessage());

        return future;
    }

    public static Future<MessageReaction.ReactionEmote> pullReaction(Message message, User user) {
        JDA jda = message.getJDA();
        CompletableFuture<MessageReaction.ReactionEmote> future = new CompletableFuture<>();
        final AtomicReference<GuildMessageReactionAddEvent> e = new AtomicReference<>((GuildMessageReactionAddEvent) pullEvent(EventsEnum.REACTION_ADD, jda));

            while (message.getChannel().getMessageById(e.get().getMessageId()).complete() != message && e.get().getUser() != user) {
                e.set((GuildMessageReactionAddEvent) pullEvent(EventsEnum.REACTION_ADD, jda));
            }

            future.complete(e.get().getReactionEmote());

        return future;
    }
}