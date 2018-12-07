package me.piggypiglet.gary.core.handlers.chat;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.FormatScanner;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.discord.MessageUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
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
        TextChannel channel = e.getChannel();
        Message message = channel.getMessageById(e.getMessageId()).complete();
        User author = message.getAuthor();

        if (!message.getAuthor().isBot()) {
            switch (e.getChannel().getId()) {
//                case Constants.REQUEST_FREE:
//                    FormatScanner scanner = new FormatScanner(message, "service", "request");
//
//                    if (!scanner.containsKeys()) {
//                        message.delete().queue();
//                        sendError(author, channel);
//                    }
//
//                    break;

            case Constants.REQUEST_PAID:
                FormatScanner scanner = new FormatScanner(message, "service", "request", "budget");

                if (!scanner.containsKeys()) {
                    message.delete().queue();
                    sendError(author, channel);
                }

                break;
//
//            case Constants.RMS:
//                break;
            }
        }
    }

    private void sendError(User author, TextChannel channel) {
        String name = channel.getName().toLowerCase();

        MessageUtils.sendMessageHaste(
                String.join("\n", Lang.getAlternateList("formats.error.message", channel.getAsMention(),
                        String.join("\n", Lang.getAlternateList("formats." + name + ".requirements")),
                        String.join("\n", Lang.getAlternateList("formats." + name + ".template")))),
                author, channel,
                String.join("\n", Lang.getAlternateList("formats.error.backup-message", author.getAsMention(), channel.getAsMention()))
        );
    }
}
