package me.piggypiglet.gary.core.util;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public class RequestUtil {
    @Inject
    private MessageUtil mutil;
    @Inject
    private WebUtil wutil;

    public RequestUtil() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }
    public void checkMessage(MessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw();
        List<String> items = new ArrayList<>();
        Stream.of(
                "service:",
                "what i want:"
        ).forEach(items::add);
        if (mutil.startsWith(msg, "[paid]")) {
            items.add("budget:");
        }
        if (mutil.contains(msg, items) && mutil.startsWith(msg, "[unpaid]/[paid]")) {
            System.out.println(e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " has successfully created a request.");
        } else {
            e.getMessage().delete().queue();
            String requirements = "- You must have '[PAID]' or '[UNPAID]' at the top of your request\n" +
                    "- You must have 'Service:' in your request\n" +
                    "- You must have 'What I want:' in your request\n" +
                    "- If paid, you must have 'Budget:' in your request";
            String example = "[PAID]/[UNPAID]\n" +
                    "Service: plugin development\n" +
                    "What I want: I need a plugin that spawns llamas all over spawn\n" +
                    "Budget (Only if paid request): $231.95";
            e.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage("Your latest request is not following the requirements for <#297996869173379072>.\n\n" +
                    "The requirements are as below:\n```" +
                    requirements +
                    "```\nFor example,\n```" +
                    example +
                    "```\nPlease edit your message below to fit the requirements:\n" +
                    "```" + msg + "```").queue(s -> {}, throwable -> {
                String hastebin = wutil.hastebin("Your latest request is not following the requirements for <#297996869173379072>.\n\n" +
                        "The requirements are as below:\n" +
                        requirements +
                        "\n\nFor example,\n\n" +
                        example +
                        "\n\nPlease edit your message below to fit the requirements:\n\n" +
                        "" + msg + "");
                if (!hastebin.equalsIgnoreCase("fail")) {
                    e.getChannel().sendMessage("**THIS MESSAGE WILL BE REMOVED IN 30 SECONDS!**\n" +
                            e.getAuthor().getAsMention() + " Your message does not follow the requirements for <#297996869173379072>, please read this:\n" +
                            hastebin).queue(message -> message.delete().completeAfter(30, TimeUnit.SECONDS));
                } else {
                    e.getChannel().sendMessage("**THIS MESSAGE WILL BE REMOVED IN 30 SECONDS!**\n" +
                            e.getAuthor().getAsMention() + " Your message does not follow the requirements for <#297996869173379072>, please fix any mistakes.\n" +
                            "hastebin.com is down and you have pm's disabled, I cannot show you your message, you will have to try remember it.").queue(message -> message.delete().completeAfter(30, TimeUnit.SECONDS));
                }
            }));
        }
    }
}
