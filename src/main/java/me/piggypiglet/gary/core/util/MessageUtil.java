package me.piggypiglet.gary.core.util;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class MessageUtil {
    @Inject private TimeUtil tutil;

    public MessageUtil() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    public String format(MessageReceivedEvent e, String str) {
        User author = e.getAuthor();
        String message = e.getMessage().getContentRaw().replace("!", "").replace("say ", "");
        return str
                .replace("%n%", "\n")
                .replace("%name%", author.getName())
                .replace("%id%", author.getId())
                .replace("%text%", message)
                .replace("%time%", tutil.getTime());
    }

    boolean contains(String msg, List<String> contain) {
        return contain.parallelStream().allMatch(msg.toLowerCase()::contains);
    }

    public boolean startsWith(String msg, String str) {
        if (str.contains("/")) {
            String[] contain = str.split("/");
            return Arrays.stream(contain).anyMatch(msg.toLowerCase()::startsWith);
        }
        return msg.toLowerCase().startsWith(str);
    }

    public String arrayToString(String[] array) {
        return Arrays.toString(array).replace("[", "").replace("]", "").replace(", ", " ");
    }

}
