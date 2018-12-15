package me.piggypiglet.gary.core.handlers.chat;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.objects.services.FormatScanner;
import me.piggypiglet.gary.core.objects.services.MinecraftServer;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.discord.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

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
                case Constants.REQUEST_FREE:
                    FormatScanner rfScanner = new FormatScanner(message);

                    if (!rfScanner.containsKeys("service", "request")) {
                        message.delete().queue();
                        sendError(author, channel, message.getContentRaw());
                    }

                    break;

            case Constants.REQUEST_PAID:
                FormatScanner rpScanner = new FormatScanner(message);

                if (!rpScanner.containsKeys("service", "request", "budget")) {
                    message.delete().queue();
                    sendError(author, channel, message.getContentRaw());
                }

                break;

            case Constants.RMS:
                FormatScanner rmsScanner = new FormatScanner(message);

                if (!rmsScanner.containsKeys("review") && !rmsScanner.containsKeys("name", "description", "ip")) {
                    message.delete().queue();
                    sendError(author, channel, message.getContentRaw());
                }

                if (rmsScanner.containsKeys()) {
                    EmbedBuilder builder = rmsScanner.toEmbed("name", "Description", "IP", "Website");
                    Map<String, String> values = rmsScanner.getValues();
                    MinecraftServer server = new MinecraftServer(values.get("ip"));

                    if (server.isSuccess()) {
                        builder.addField("Extras:", "Premium: " + server.isPremium() + "\nVersion: " + server.getVersion(), false);
                        builder.setFooter("Posted by ", author.getAsMention());
                        builder.setThumbnail("attachment://server.png");
                        InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(server.getFavicon().replace("\n", "").split(",")[1]));
                        channel.sendFile(stream, "server.png", new MessageBuilder().setEmbed(builder.build()).build()).queue(s -> Arrays.stream(Constants.RATINGS).forEach(em -> s.addReaction(e.getJDA().getEmoteById(em)).queue()));

                        try {
                            stream.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        String msg = Lang.getString("formats.rate-my-server.server-error", author.getAsMention());
                        MessageUtils.sendMessage(msg, author, channel, msg);
                    }

                    message.delete().queue();
                }

                break;
            }
        }
    }

    private void sendError(User author, TextChannel channel, String message) {
        String name = channel.getName().toLowerCase();

        MessageUtils.sendMessageHaste(
                String.join("\n", Lang.getAlternateList("formats.error.message", channel.getAsMention(),
                        String.join("\n", Lang.getAlternateList("formats." + name + ".requirements")),
                        String.join("\n", Lang.getAlternateList("formats." + name + ".template")),
                        message)),
                author, channel,
                String.join("\n", Lang.getAlternateList("formats.error.backup-message", author.getAsMention(), channel.getAsMention()))
        );
    }
}
