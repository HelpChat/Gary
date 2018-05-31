package me.piggypiglet.gary.core.utils.message;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.web.WebUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class RequestUtils {
    @Inject private MessageUtils mutil;
    @Inject private WebUtils wutil;

    private Logger logger;

    public RequestUtils() {
        logger = LoggerFactory.getLogger("RequestUtils");
    }

    private boolean isPaid(long channelId) {
        return channelId == Constants.REQUEST_PAID;
    }

    public void checkMessage(GenericMessageEvent e, long channelId) {
        User author = null;
        Message message = null;

        if (e instanceof MessageReceivedEvent) {
            MessageReceivedEvent ev = (MessageReceivedEvent) e;
            author = ev.getAuthor();
            message = ev.getMessage();
        }

        if (e instanceof MessageUpdateEvent) {
            MessageUpdateEvent ev = (MessageUpdateEvent) e;
            author = ev.getAuthor();
            message = ev.getMessage();
        }

        if (author != null && message != null) {
            MessageChannel channel = e.getChannel();
            String msg = message.getContentStripped();

            List<String> items = new ArrayList<>();
            Stream.of("service:", "request:").forEach(items::add);

            boolean isPaid = isPaid(channelId);

            if (isPaid) items.add("budget:");

            if (mutil.contains(msg, items)) {
                logger.info(author.getName() + "#" + author.getDiscriminator() + " has successfully created a request.");
            } else {
                message.delete().queue();
                sendHelp(author, msg, channel, isPaid);
            }
        }
    }

    private void sendHelp(User author, String msg, MessageChannel channel, boolean isPaid) {
        StringBuilder requirements = new StringBuilder();
        Stream.of(
                "- You must have 'Service:' in your request\n",
                "- You must have 'Request:' in your request"
        ).forEach(requirements::append);
        if (isPaid) requirements.append("\n- You must have 'Budget:' in your request");

        StringBuilder example = new StringBuilder();
        Stream.of(
                "Service: plugin development\n",
                "Request: I need a plugin that spawns llamas all over spawn"
        ).forEach(example::append);
        if (isPaid) example.append("\nBudget: $231.95");

        final String channelId = channel.getId();
        author.openPrivateChannel().queue(privateChannel -> {
            String string = "Your latest request is not following the requirements for <#" + channelId + ">.\n\n" +
                    "The requirements are as below:\n```" + requirements + "```\nFor example,\n```" + example +
                    "```\nPlease edit your message below to fit the requirements:\n```" + msg + "```";

            privateChannel.sendMessage(string).queue(message1 -> {
            }, throwable -> {
                String hastebin = wutil.hastebin("Your latest request is not following the requirements for #" + channel.getName() + ".\n\n" +
                        "The requirements are as below:\n" + requirements + "\n\nFor example,\n\n" + example +
                        "\n\nPlease edit your message below to fit the requirements:\n\n" + msg);

                String toSend = !hastebin.equals("fail")
                        ? "**THIS MESSAGE WILL BE REMOVED IN 30 SECONDS!**\n" + author.getAsMention()
                        + " Your message does not follow the requirements for <#" + channelId + ">, "
                        + "please read this:\n" + hastebin
                        : "**THIS MESSAGE WILL BE REMOVED IN 30 SECONDS!**\n" + author.getAsMention()
                        + " Your message does not follow the requirements for <#" + channelId + ">, "
                        + "please fix any mistakes.\nhastebin.com is down and you have pm's disabled, "
                        + "I cannot show you your message, you will have to try remember it.";

                channel.sendMessage(toSend).queue(message1 -> message1.delete().completeAfter(30, TimeUnit.SECONDS));
            });
        });
    }
}