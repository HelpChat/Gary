package me.piggypiglet.gary.core.utils.misc;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.web.WebUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ChannelUtils {
    @Inject private WebUtils webUtils;
    @Inject private TimeUtils timeUtils;

    public void purgeChannel(TextChannel channel, long messageId, int limit, boolean before) {
        JDA jda = channel.getJDA();
        List<Message> messages;
        if (before) {
            messages = channel.getHistoryBefore(messageId, limit).complete().getRetrievedHistory();
        } else {
            messages = channel.getHistoryAfter(messageId, limit).complete().getRetrievedHistory();
        }

        StringBuilder message = new StringBuilder();
        messages.forEach(msg -> message.append(msg.getContentRaw()).append("\n\n")); // testing, will be removed after next month if successful.

        MessageEmbed.Field field = new MessageEmbed.Field("URL:", webUtils.hastebin(message.toString()), false);
        MessageEmbed msg = new EmbedBuilder()
                .setTitle("Purge")
                .setFooter("Purged #" + channel.getName() + " at " + timeUtils.getTime(), Constants.AVATAR)
                .addField(field)
                .build();

        jda.getTextChannelById(Constants.PIG).sendMessage(msg).queue();
        channel.deleteMessages(messages).queue();

        if (before) channel.getMessageById(messageId).complete().delete().queue();
    }

    public TextChannel getTextChannel(JDA jda, long id) {
        return jda.getTextChannelById(id);
    }
}
