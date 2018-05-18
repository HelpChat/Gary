package me.piggypiglet.gary.core.logging.types;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.storage.mysql.tables.Messages;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.time.ZonedDateTime;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MessageBulkDelete extends Logger {
    @Inject private Messages messages;

    public MessageBulkDelete() {
        super(EventsEnum.MESSAGE_BULK_DELETE);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MessageEmbed send() {
        if (getOther()[0] instanceof Channel && getOther()[1] instanceof List<?>) {
            for (Object object : (List<?>) getOther()[1]) {
                if (object instanceof String) {
                    Channel channel = (Channel) getOther()[0];
                    List<String> amount = ((List<String>) getOther()[1]);

                    amount.stream().map(Long::parseLong).forEach(messages::deleteMessage);

                    return new EmbedBuilder()
                            .setTitle(getGuild().getName())
                            .setColor(Constants.BLUE)
                            .setDescription("**Bulk Delete in <#" + channel.getIdLong() + ">, " + amount.size() + " messages deleted**")
                            .setTimestamp(ZonedDateTime.now())
                            .build();
                }
            }
        }

        return null;
    }
}
