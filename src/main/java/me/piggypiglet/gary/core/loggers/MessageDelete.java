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
public final class MessageDelete extends Logger {
    public MessageDelete() {
        super(EventsEnum.MESSAGE_DELETE);
    }

    @Override
    protected MessageEmbed send() {
        return new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setColor(Constants.RED)
                .setDescription("**Message sent by " + user.getAsMention() + " deleted in " + channel.getAsMention() + ".**\n" + message.getContentRaw())
                .setFooter("ID: " + message.getId(), null)
                .setTimestamp(ZonedDateTime.now())
                .build();
    }
}
