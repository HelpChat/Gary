package me.piggypiglet.gary.core.utils.discord;

import me.piggypiglet.gary.core.utils.http.HasteUtils;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import javax.annotation.Nonnull;

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
    public static void sendMessageHaste(@Nonnull final String msg, @Nonnull User user, @Nonnull final TextChannel channel, @Nonnull final String backupMsg) {
        user.openPrivateChannel().queue(c -> c.sendMessage(msg).queue(null, t -> channel.sendMessage(String.format(backupMsg, HasteUtils.haste(msg))).queue()));
    }
}
