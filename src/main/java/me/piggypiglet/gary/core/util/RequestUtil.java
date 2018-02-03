package me.piggypiglet.gary.core.util;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public class RequestUtil {
    @Inject
    private MessageUtil mutil;

    public RequestUtil() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }
    public void checkMessage(MessageReceivedEvent e) {
        e.getChannel().sendMessage("test").queue();
        String msg = e.getMessage().getContentRaw();
        List<String> items = new ArrayList<>();
        Stream.of(
                "service:",
                "what i want:"
        ).forEach(items::add);
        if (mutil.contains(msg, items) && mutil.startsWith("[unpaid]/[paid]")) {
            if (msg.toLowerCase().startsWith("[paid]")) {
                items.add("budget:");
            }
        } else {
            e.getMessage().delete().queue();
            // TODO: Setup pm and hastebin options for when user doesn't meet requirements
        }
    }
}
