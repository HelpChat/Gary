package me.piggypiglet.gary.core.utils.message;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.utils.web.WebUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ErrorUtils {
    @Inject private WebUtils webUtils;

    public void checkMessage(MessageReceivedEvent e) {
        User author = e.getAuthor();
        Message message = e.getMessage();
        String messageContent = message.getContentRaw();

        if (checks(messageContent)) {
            if (messageContent.contains("WARN]:")) {
                Objects.requireNonNull(separateErrors(messageContent, "WARN]:")).forEach(error -> e.getChannel().sendMessage(webUtils.hastebin(error)).queue());
            }
            if (messageContent.contains("ERROR]:")) {
                Objects.requireNonNull(separateErrors(messageContent, "ERROR]:"));
            }
        }
    }

    private boolean checks(String str) {
        List<String> items = new ArrayList<>();
        Stream.of("[", "]", "java.lang").forEach(items::add);

        return str.split("at").length > 5 && StringUtils.contains(str, items);
    }

    private List<String> separateErrors(String message, String item) {
        // TODO: Fix cutting last line off if more than 2 errors, make more efficient and better in general.

        if (message.contains(item)) {
            message = message.replace(item, item + "-WARN-");
            String[] split = message.split(item.toUpperCase());
            List<String> errors = new ArrayList<>();

            if (split.length >= 2) {
                for (String str : split) {
                    str = str.replace("-WARN-", "WARN]:");
                    String[] split2 = str.split("]");
                    List<String> temp = new ArrayList<>();

                    for (int i = 0; i <= (split2.length - 2); i++) {
                        temp.add("-REPLACE-" + split2[i]);
                    }

                    temp.forEach(error -> { if (!error.isEmpty()) errors.add(error); });
                }
            }

            return errors;
        }
        return null;
    }
}
