package me.piggypiglet.gary.core.objects.questionnaire;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;

import java.util.concurrent.Future;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Response {
    @Getter private final String key;
    @Setter @Getter private Future<MessageReaction> reaction;
    @Setter @Getter private Future<Message> message;

    public Response(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        try {
            StringBuilder stringBuilder = new StringBuilder("[");

            if (reaction != null && reaction.isDone()) {
                stringBuilder.append("unicode:(").append(reaction.get()).append(")");
            }

            if (message != null && message.isDone()) {
                stringBuilder.append("message:(").append(message.get().getContentRaw()).append(")");
            }

            return stringBuilder.append("]").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "[null]";
    }
}
