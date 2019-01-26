package me.piggypiglet.gary.core.utils.discord;

import com.google.inject.Inject;
import emoji4j.EmojiUtils;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.http.HasteUtils;
import net.dv8tion.jda.api.entities.*;
import sh.okx.timeapi.TimeAPI;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MessageUtils {
    @Inject private static GaryBot garyBot;

    /**
     * Attempt to send a private message to a user. If their private messages are disabled, will send a message to the channel with a link to a hastebin containing msg.
     * @param msg The message that is to be sent.
     * @param user The user the message is for.
     * @param channel Backup channel to send a message to if pm fails.
     * @param backupMsg Backup message that will be hastebinned if pm fails.
     */
    public static Message sendMessageHaste(String msg, User user, TextChannel channel, String backupMsg) {
        CompletableFuture<Message> message = new CompletableFuture<>();
        user.openPrivateChannel().queue(c -> c.sendMessage(msg).queue(null, t -> channel.sendMessage(backupMsg.replace("haste", HasteUtils.haste(msg))).queue(message::complete)));

        //noinspection StatementWithEmptyBody
        while (!message.isDone()) {}

        try {
            return message.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void sendMessageHaste(String msg, User user, TextChannel channel, String backupMsg, TimeAPI time) {
        Message message = sendMessageHaste(msg, user, channel, backupMsg);

        if (message != null) {
            message.delete().queueAfter(time.getSeconds(), TimeUnit.SECONDS);
        }
    }

    public static void sendMessage(String msg, User user, TextChannel channel, String backupMsg) {
        user.openPrivateChannel().queue(c -> c.sendMessage(msg).queue(null, t -> channel.sendMessage(backupMsg).queue(s -> s.delete().queueAfter(30, TimeUnit.SECONDS))));
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

    public static Object getEmoteObject(String emote) {
        Guild guild = garyBot.getJda().getGuildById(Constants.GUILD);

        if (EmojiUtils.isEmoji(emote)) {
            return emote;
        } else {
            return guild.getEmoteById(Stream.of(emote.replaceAll("^\\D+", "").split("\\D+")).mapToLong(Long::parseLong).max().orElse(0L));
        }
    }

    public static Message getFutureMessage(String message, TextChannel channel) {
        CompletableFuture<Message> msg = new CompletableFuture<>();
        channel.sendMessage(message).queue(msg::complete);

        //noinspection StatementWithEmptyBody
        while (!msg.isDone()) {}

        try {
            return msg.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
