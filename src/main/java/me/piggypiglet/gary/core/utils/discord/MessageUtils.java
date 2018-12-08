package me.piggypiglet.gary.core.utils.discord;

import me.piggypiglet.gary.core.utils.http.HasteUtils;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MessageUtils {
    /**
     * Attempt to send a private message to a user. If their private messages are disabled, will send a message to the channel with a link to a hastebin containing msg.
     * @param msg The message that is to be sent.
     * @param user The user the message is for.
     * @param channel Backup channel to send a message to if pm fails.
     * @param backupMsg Backup message that will be hastebinned if pm fails.
     */
    public static void sendMessageHaste(String msg, User user, TextChannel channel, String backupMsg) {
        System.out.println("test1");
        user.openPrivateChannel().queue(c -> {
            System.out.println("test");
            c.sendMessage(msg).queue(s -> {}, t -> channel.sendMessage(backupMsg.replace("haste", HasteUtils.haste(msg))).queue(s -> s.delete().queueAfter(30, TimeUnit.SECONDS)));
        });
    }

    public static void addEmoteObjectToMessage(@Nonnull final Message message, @Nonnull final List<Object> emotes) {
        emotes.forEach(emote -> {
            if (emote instanceof String) {
                message.addReaction((String) emote).queue();
            } else if (emote instanceof Emote) {
                message.addReaction((Emote) emote).queue();
            }
        });
    }
}
