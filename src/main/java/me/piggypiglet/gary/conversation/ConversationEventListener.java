package me.piggypiglet.gary.conversation;

import com.google.common.cache.LoadingCache;
import com.google.inject.Singleton;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class ConversationEventListener extends ListenerAdapter {
    private final LoadingCache<>

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {

    }
}
