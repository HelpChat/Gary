package me.piggypiglet.gary.core.utils.channel;

import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.time.Month;
import java.util.Calendar;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ChannelUtils {
    public void purgeChannel(JDA jda, long channelId, long messageId) {
        Guild guild = jda.getGuildById(Constants.HELP_CHAT);
        List<Message> messages = guild.getTextChannelById(channelId).getHistoryAfter(messageId, 100).complete().getRetrievedHistory();
        messages.forEach(msg -> guild.getTextChannelById(Constants.PIG).sendMessage(msg.getContentRaw()).queue()); // testing, will be removed after next month if successful.
        messages.forEach(msg -> msg.delete().queue());

        Calendar calendar = Calendar.getInstance();
        String date = Month.of(calendar.get(Calendar.MONTH) + 1).toString().toLowerCase();
        guild.getTextChannelById(channelId).getMessageById(messageId).complete().editMessage(":information_source:  Last Reset: " + date.substring(0, 1).toUpperCase() + date.substring(1) + " 1st").queue();
    }
}
