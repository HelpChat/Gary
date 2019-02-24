package me.piggypiglet.gary.core.handlers.misc.services;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.objects.services.FormatScanner;
import me.piggypiglet.gary.core.objects.services.MinecraftServer;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.discord.MessageUtils;
import me.piggypiglet.gary.core.utils.discord.RoleUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import sh.okx.timeapi.TimeAPI;

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
    protected void execute(GenericEvent event) {
        GenericGuildMessageEvent e = (GenericGuildMessageEvent) event;
        TextChannel channel = e.getChannel();
        Message message = channel.retrieveMessageById(e.getMessageId()).complete();
        User author = message.getAuthor();
        FormatScanner scanner = new FormatScanner(message);

        if (!author.isBot()) {
            boolean isRequest = false;

            switch (e.getChannel().getName()) {
                case "offer-services":
                    RoleUtils.addRole(message.getMember(), Constants.OS_MUTE);
                    break;

                case "request-free":
                    if (!scanner.containsKeys("service", "request")) {
                        message.delete().queue();
                        sendError(author, channel, message.getContentRaw());
                    } else {
                        RoleUtils.addRole(message.getMember(), Constants.RF_MUTE);
                    }

                    isRequest = true;
                    break;

                case "request-paid":
                    if (scanner.containsKeys("service", "request", "budget")) {
                        message.delete().queue();
                        sendError(author, channel, message.getContentRaw());
                    } else {
                        RoleUtils.addRole(message.getMember(), Constants.RP_MUTE);
                    }

                    isRequest = true;
                    break;

                case "rate-my-server":
                    if (!scanner.containsKeys("review") && !scanner.containsKeys("name", "description", "ip")) {
                        message.delete().queue();
                        sendError(author, channel, message.getContentRaw());
                    }

                    if (scanner.containsKeys("name", "description", "ip")) {
                        EmbedBuilder builder = scanner.toEmbed("name", "Description", "IP", "Website");
                        Map<String, String> values = scanner.getValues();
                        MinecraftServer server = new MinecraftServer(values.get("ip"));

                        if (server.isSuccess()) {
                            builder.addField("Extras:", "Premium: " + server.isPremium() + "\nVersion: " + server.getVersion(), false);
                            builder.setFooter("Posted by " + author.getName() + "#" + author.getDiscriminator(), null);

                            if (!server.getFavicon().equalsIgnoreCase("null")) {
                                builder.setThumbnail("attachment://server.png");
                                InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(server.getFavicon().replace("\n", "").split(",")[1]));
                                channel.sendFile(stream, "server.png", new MessageBuilder().setEmbed(builder.build()).build()).queue(s -> Arrays.stream(Constants.RATINGS).forEach(em -> s.addReaction(e.getJDA().getEmoteById(em)).queue()));

                                try {
                                    stream.close();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                channel.sendMessage(builder.build()).queue(s -> Arrays.stream(Constants.RATINGS).forEach(em -> s.addReaction(e.getJDA().getEmoteById(em)).queue()));
                            }
                        } else {
                            String msg = Lang.getString("formats.rate-my-server.server-error", author.getAsMention());
                            MessageUtils.sendMessage(msg, author, channel, msg);
                        }

                        message.delete().queue();
                        RoleUtils.addRole(message.getMember(), Constants.RMS_MUTE);
                    }

                    isRequest = true;
                    break;
            }

            if (isRequest) {
                message.addReaction("\uD83D\uDEE0").queue();
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
                String.join("\n", Lang.getAlternateList("formats.error.backup-message", author.getAsMention(), channel.getAsMention())),
                new TimeAPI("30secs")
        );
    }
}
