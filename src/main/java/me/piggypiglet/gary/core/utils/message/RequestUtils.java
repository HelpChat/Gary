package me.piggypiglet.gary.core.utils.message;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.utils.web.WebUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;

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

    public void checkMessage(GenericMessageEvent e) {
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
            String msg = message.getContentRaw();

            List<String> items = new ArrayList<>();
            Stream.of("service:", "what i want:").forEach(items::add);

            if (mutil.startsWith(msg, "[paid]")) {
                items.add("budget:");
            }

            if (mutil.contains(msg, items) && mutil.startsWith(msg, "[unpaid]/[paid]")) {
                System.out.println(author.getName() + "#" + author.getDiscriminator() + " has successfully created a request.");
            } else {
                message.delete().queue();
                String requirements = "- You must have '[PAID]' or '[UNPAID]' at the top of your request\n" +
                        "- You must have 'Service:' in your request\n" +
                        "- You must have 'What I want:' in your request\n" +
                        "- If paid, you must have 'Budget:' in your request";
                String example = "[PAID]/[UNPAID]\n" +
                        "Service: plugin development\n" +
                        "What I want: I need a plugin that spawns llamas all over spawn\n" +
                        "Budget (Only if paid request): $231.95";

                User finalAuthor = author;
                author.openPrivateChannel().queue(privateChannel -> {
                    String string = "Your latest request is not following the requirements for <#297996869173379072>.\n\n" +
                            "The requirements are as below:\n```" + requirements + "```\nFor example,\n```" + example +
                            "```\nPlease edit your message below to fit the requirements:\n```" + msg + "```";

                    privateChannel.sendMessage(string).queue(message1 -> {
                    }, throwable -> {
                        String hastebin = wutil.hastebin("Your latest request is not following the requirements for <#297996869173379072>.\n\n" +
                                "The requirements are as below:\n" + requirements + "\n\nFor example,\n\n" + example +
                                "\n\nPlease edit your message below to fit the requirements:\n\n" + msg);

                        String toSend = !hastebin.equals("fail")
                                ? "**THIS MESSAGE WILL BE REMOVED IN 30 SECONDS!**\n" + finalAuthor.getAsMention()
                                + " Your message does not follow the requirements for <#297996869173379072>, "
                                + "please read this:\n" + hastebin
                                : "**THIS MESSAGE WILL BE REMOVED IN 30 SECONDS!**\n" + finalAuthor.getAsMention()
                                + " Your message does not follow the requirements for <#297996869173379072>, "
                                + "please fix any mistakes.\nhastebin.com is down and you have pm's disabled, "
                                + "I cannot show you your message, you will have to try remember it.";

                        channel.sendMessage(toSend).queue(message1 -> message1.delete().completeAfter(30, TimeUnit.SECONDS));
                    });
                });
            }
        }
    }

}
