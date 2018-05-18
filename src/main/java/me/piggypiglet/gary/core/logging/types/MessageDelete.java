package me.piggypiglet.gary.core.logging.types;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.storage.mysql.tables.Messages;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MessageDelete extends Logger {
    @Inject private Messages messages;

    public MessageDelete() {
        super(EventsEnum.MESSAGE_DELETE);
    }

    @Override
    protected MessageEmbed send() {
        if (getOther()[0] instanceof Channel && getOther()[1] instanceof Long) {
            Channel channel = (Channel) getOther()[0];
            long messageId = (Long) getOther()[1];
            User user = messages.getUser(getGuild(), messageId);
            String message = messages.getMessage(messageId);

            if (user != null && message != null) {
                messages.deleteMessage(messageId);

                return new EmbedBuilder()
                        .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl())
                        .setColor(Constants.RED)
                        .setDescription("**Message sent by " + user.getAsMention() + " deleted in <#" + channel.getIdLong() + ">**\n" + message)
                        .setFooter("ID: " + messageId, null)
                        .setTimestamp(ZonedDateTime.now())
                        .build();
            }
        }

        return null;
    }
}
