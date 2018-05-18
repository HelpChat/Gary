package me.piggypiglet.gary.core.logging.types;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.storage.mysql.tables.Messages;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MessageEdit extends Logger {
    @Inject private Messages messages;

    public MessageEdit() {
        super(EventsEnum.MESSAGE_EDIT);
    }

    @Override
    protected MessageEmbed send() {
        if (getOther()[0] instanceof User && getOther()[1] instanceof Channel && getOther()[2] instanceof Message) {
            User author = (User) getOther()[0];
            Channel channel = (Channel) getOther()[1];
            Message message = (Message) getOther()[2];
            long messageId = message.getIdLong();

            messages.editMessage(message);

            String currentMessage = message.getContentRaw().length() >= 229 ? message.getContentRaw().substring(0, 229) : message.getContentRaw();
            String previousMessage = messages.getPreviousMessage(messageId);

            if (currentMessage.length() == 229) {
                currentMessage = currentMessage + "...";
            }

            if (!previousMessage.isEmpty()) {
                MessageEmbed.Field before = new MessageEmbed.Field("Before", previousMessage, false);
                MessageEmbed.Field after = new MessageEmbed.Field("After", currentMessage, false);

                return new EmbedBuilder()
                        .setAuthor(author.getName() + "#" + author.getDiscriminator(), null, author.getAvatarUrl())
                        .setColor(Constants.BLUE)
                        .setDescription("**Message edited in <#" + channel.getId() + ">**")
                        .addField(before)
                        .addField(after)
                        .setFooter("User ID: " + author.getId(), null)
                        .setTimestamp(ZonedDateTime.now())
                        .build();
            }
        }

        return null;
    }
}
