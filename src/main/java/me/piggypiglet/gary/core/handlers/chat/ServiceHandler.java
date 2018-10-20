package me.piggypiglet.gary.core.handlers.chat;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.FormatScanner;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.utils.discord.MessageUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServiceHandler extends GEvent {
    public ServiceHandler() {
        super(EventsEnum.MESSAGE_CREATE, EventsEnum.MESSAGE_EDIT);
    }

    @Override
    protected void execute(Event event) {
        GenericGuildMessageEvent e = (GenericGuildMessageEvent) event;
        Message message = e.getChannel().getMessageById(e.getMessageId()).complete();

        if (!message.getAuthor().isBot()) {
            switch (e.getChannel().getId()) {
                case Constants.REQUEST_FREE:
                    FormatScanner scanner = new FormatScanner(message, "service", "request");
                    TextChannel channel = e.getChannel();
                    message.delete().queue();

                    if (scanner.containsKeys()) {
                        channel.sendMessage(scanner.toEmbed("service", message.getAuthor().getEffectiveAvatarUrl(), "")).queue();
                    } else {
                        MessageUtils.sendMessageHaste("u dun goofed boi", message.getAuthor(), e.getChannel(), "u really dun goofed boi: %s");
                    }
                    break;

//            case Constants.REQUEST_PAID:
//                break;
//
//            case Constants.RMS:
//                break;
            }
        }
    }
}
