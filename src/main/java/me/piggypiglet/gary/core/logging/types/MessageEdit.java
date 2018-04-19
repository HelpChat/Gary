package me.piggypiglet.gary.core.logging.types;

import co.aikar.idb.DB;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.LogType;
import me.piggypiglet.gary.core.storage.tables.Messages;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class MessageEdit extends Logger {
    @Inject private Messages messages;

    public MessageEdit() {
        super(LogType.MESSAGE_EDIT);
    }

    @Override
    protected MessageEmbed send() {
        messages.editMessage(getMessage());

        User user = getUser();
        long message_id = getMessageId();
        String current_message = getMessage().getContentRaw().length() >= 229 ? getMessage().getContentRaw().substring(0, 229) : getMessage().getContentRaw();
        String previous_message = "";

        if (current_message.length() == 229) {
            current_message = current_message + "...";
        }

        try {
            previous_message = (String) DB.getFirstColumnAsync("SELECT `previous_message` FROM `gary_messages` WHERE `message_id`=?", message_id).get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!previous_message.isEmpty()) {
            MessageEmbed.Field before = new MessageEmbed.Field("Before", previous_message, false);
            MessageEmbed.Field after = new MessageEmbed.Field("After", current_message, false);

            return new EmbedBuilder()
                    .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl())
                    .setColor(Constants.BLUE)
                    .setDescription("**Message edited in <#" + getChannel().getIdLong() + ">**")
                    .addField(before)
                    .addField(after)
                    .setFooter("User ID: " + user.getIdLong(), null)
                    .setTimestamp(ZonedDateTime.now())
                    .build();
        }

        return null;
    }
}
