package me.piggypiglet.gary.core.loggers;

import me.piggypiglet.gary.core.framework.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class VoiceJoin extends Logger {
    public VoiceJoin() {
        super(EventsEnum.VOICE_JOIN);
    }

    @Override
    protected MessageEmbed send() {
        return new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setColor(Constants.GREEN)
                .setDescription(user.getAsMention() + " **joined voice channel " + channel.getAsMention() + "**")
                .setFooter("ID: " + user.getId(), null)
                .setTimestamp(ZonedDateTime.now())
                .build();
    }
}
