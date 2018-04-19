package me.piggypiglet.gary.core.logging.types;

import me.piggypiglet.gary.core.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.LogType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class VoiceJoin extends Logger {
    public VoiceJoin() {
        super(LogType.VOICE_JOIN);
    }

    @Override
    protected MessageEmbed send() {
        User user = getUser();
        long channelId = getChannel().getIdLong();

        return new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setColor(Constants.BLUE)
                .setDescription(user.getAsMention() + " **joined voice channel <#" + channelId + ">**")
                .setFooter("ID: " + channelId, null)
                .setTimestamp(ZonedDateTime.now())
                .build();
    }
}
