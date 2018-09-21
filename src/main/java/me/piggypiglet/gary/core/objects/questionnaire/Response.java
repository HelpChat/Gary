package me.piggypiglet.gary.core.objects.questionnaire;

import lombok.Getter;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;

import java.util.concurrent.Future;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Response {
    @Getter private final String key;
    @Getter private MessageReaction reaction;
    @Getter private Message message;

    public Response(String key) {
        this.key = key;
    }

    public void setReaction(Future<MessageReaction> reaction) {
        while (!reaction.isDone());

        try {
            this.reaction = reaction.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(Future<Message> message) {
        while (!message.isDone());

        try {
            this.message = message.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        try {
            StringBuilder stringBuilder = new StringBuilder("[");

            if (reaction != null) {
                stringBuilder.append("unicode:(").append(reaction).append(")");
            }

            if (message != null) {
                stringBuilder.append("message:(").append(message.getContentRaw()).append(")");
            }

            return stringBuilder.append("]").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "[null]";
    }
}
