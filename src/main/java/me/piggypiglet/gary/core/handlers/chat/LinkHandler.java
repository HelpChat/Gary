package me.piggypiglet.gary.core.handlers.chat;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.storage.file.GFile;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class LinkHandler extends GEvent {
    @Inject private GFile gFile;

    public LinkHandler() {
        super(EventsEnum.MESSAGE_CREATE);
    }

    @Override
    protected void execute(GenericEvent event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;

        if (e.getMember().getRoles().isEmpty() && !e.getAuthor().isBot()) {
            String message = e.getMessage().getContentRaw();
            List<String> allowedLinks = gFile.getFileConfiguration("config").getStringList("allowed-links");

            if (StringUtils.contains(message, "https:/http:") && !StringUtils.contains(message, allowedLinks)) {
                e.getMessage().delete().queue();
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " you are not allowed to post non-whitelisted links till you level up.").queue(s -> s.delete().queueAfter(15, TimeUnit.SECONDS));
            }
        }
    }
}
