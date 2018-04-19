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
public class MessageDelete extends Logger {
    @Inject private Messages messages;

    public MessageDelete() {
        super(LogType.MESSAGE_DELETE);
    }

    @Override
    protected MessageEmbed send() {
        User user = null;
        long message_id = getMessageId();
        String message_ = "";

        try {
            if (DB.getFirstColumnAsync("SELECT `discord_id` FROM `gary_messages` WHERE `message_id`=?", message_id).get() != null) {
                user = getGuild().getMemberById((Long) DB.getFirstColumnAsync("SELECT `discord_id` FROM `gary_messages` WHERE `message_id`=?", message_id).get()).getUser();
            }

            message_ = (String) DB.getFirstColumnAsync("SELECT `current_message` FROM `gary_messages` WHERE `message_id`=?", message_id).get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (user != null && !message_.isEmpty()) {
            messages.deleteMessage(getMessageId());

            return new EmbedBuilder()
                    .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl())
                    .setColor(Constants.RED)
                    .setDescription("**Message sent by " + user.getAsMention() + " deleted in <#" + getChannel().getIdLong() + ">**\n" + message_)
                    .setFooter("ID: " + message_id, null)
                    .setTimestamp(ZonedDateTime.now())
                    .build();
        }

        return null;
    }
}
