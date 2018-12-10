package me.piggypiglet.gary.core.handlers.chat;

import me.piggypiglet.gary.core.handlers.GEvent;
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
//                        sendError(author, channel, message.getContentRaw());
//                    }
//
//                    break;

//            case Constants.REQUEST_PAID:
//                FormatScanner scanner = new FormatScanner(message, "service", "request", "budget");
//
//                if (!scanner.containsKeys()) {
//                    message.delete().queue();
//                    sendError(author, channel, message.getContentRaw());
//                }
//
//                break;
//
//            case Constants.RMS:
//                FormatScanner scanner = new FormatScanner(message);
//
//                if (!scanner.containsKeys("review") && !scanner.containsKeys("name", "description", "ip")) {
//                    message.delete().queue();
//                    sendError(author, channel, message.getContentRaw());
//                }
//
//                if (scanner.containsKeys()) {
//                    EmbedBuilder builder = scanner.toEmbed("name", "Description", "IP", "Website");
//                    Map<String, String> values = scanner.getValues();
//                    MinecraftServer server = new MinecraftServer(values.get("ip"));
//
//                    if (server.isSuccess()) {
//                        builder.addField("Extras:", "Premium: " + server.isPremium() + "\nVersion: " + server.getVersion(), false);
//                        builder.setThumbnail("attachment://server.png");
//                        InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(server.getFavicon().split(",")[1]));
//                        channel.sendFile(stream, "server.png", new MessageBuilder().setEmbed(builder.build()).build()).queue(s -> Arrays.stream(Constants.RATINGS).forEach(em -> s.addReaction(e.getJDA().getEmoteById(em)).queue()));
//
//                        try {
//                            stream.close();
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                    } else {
//                        String msg = Lang.getString("formats.rate-my-server.server-error", author.getAsMention());
//                        MessageUtils.sendMessage(msg, author, channel, msg);
//                    }
//
//                    message.delete().queue();
//                }
//
//                break;
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
