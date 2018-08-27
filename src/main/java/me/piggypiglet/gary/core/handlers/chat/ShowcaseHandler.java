package me.piggypiglet.gary.core.handlers.chat;

import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ShowcaseHandler {
    public void check(GuildMessageReceivedEvent e) {
        if (e.getChannel().getIdLong() == Constants.SHOWCASE) {
            Guild guild = e.getGuild();

            Stream.of(
                    Constants.PLUS_1,
                    Constants.MINUS_1
            ).forEach(em -> e.getMessage().addReaction(guild.getEmoteById(em)).queue());
        }
    }
}
