package me.piggypiglet.gary.core.utils.channel;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.utils.web.WebUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class RMSUtils {
    @Inject private MessageUtils mutil;
    @Inject private WebUtils wutil;

    public void checkMessage(MessageReceivedEvent e) {
        User author = e.getAuthor();
        Message message = e.getMessage();
        MessageChannel channel = e.getChannel();
        String msg = message.getContentRaw();

        List<String> items = new ArrayList<>();
        Stream.of("name:", "ip:", "description:").forEach(items::add);

        if (mutil.contains(msg, items) || mutil.startsWith(msg, "[REVIEW]")) {
            System.out.println(author.getName() + "#" + author.getDiscriminator() + " has successfully created a request.");
        } else {
            message.delete().queue();
            String requirements = "**Reviews** - The message must start with [REVIEW]\n" +
                    "**RMS Request** - You must have 'Name:' in your request.\n" +
                    "**RMS Request** - You must have 'IP:' in your request\n" +
                    "**RMS Request** - Not required, but we suggest having 'Website:' in your request.\n" +
                    "**RMS Request** - You must have 'Description:' in your request.";
            String exampleReview = "[REVIEW]\n" +
                    "message";
            String exampleRMS = "Name:\n" +
                    "IP:\n" +
                    "Website (optional):\n" +
                    "Description:";

            author.openPrivateChannel().queue(privateChannel -> {
                String string = "Your latest request is not following the requirements for <#424460627663126538>.\n" +
                        "The requirements are as below:\n```" + requirements + "```\nFor example, your review needs to look like this: \n```" + exampleReview + "```\n" +
                        "RMS requests need to look like this:\n```" + exampleRMS + "```\n" +
                        "Please edit your message below to fit the requirements:\n```" + msg + "```";

                privateChannel.sendMessage(string).queue(message1 -> {
                }, throwable -> {
                    String hastebin = wutil.hastebin(string.replace("`", ""));

                    String toSend = !hastebin.equals("fail")
                            ? "**THIS MESSAGE WILL BE REMOVED IN 30 SECONDS!**\n" + author.getAsMention()
                            + " Your message does not follow the requirements for <#424460627663126538>, "
                            + "please read this:\n" + hastebin
                            : "**THIS MESSAGE WILL BE REMOVED IN 30 SECONDS!**\n" + author.getAsMention()
                            + " Your message does not follow the requirements for <#424460627663126538>, "
                            + "please fix any mistakes.\nhastebin.com is down and you have pm's disabled, "
                            + "I cannot show you your message, you will have to try remember it.";

                    channel.sendMessage(toSend).queue(message1 -> message1.delete().completeAfter(30, TimeUnit.SECONDS));
                });
            });
        }
    }

}
