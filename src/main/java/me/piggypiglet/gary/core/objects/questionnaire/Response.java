package me.piggypiglet.gary.core.objects.questionnaire;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.concurrent.Future;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@SuppressWarnings("StatementWithEmptyBody")
public final class Response {
    @Getter private final String key;
    @Getter private MessageReaction reaction;
    @Getter private Message message;
    @Getter private int integer = 0;

    public Response(String key) {
        this.key = key;
    }

    /**
     * Sets the user's reaction.
     * @param reaction The user's reaction.
     */
    public void setReaction(Future<MessageReaction> reaction) {
        while (!reaction.isDone());

        try {
            this.reaction = reaction.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the user's message.
     * @param message The user's message.
     */
    public void setMessage(Future<Message> message) {
        while (!message.isDone());

        try {
            this.message = message.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the user's integer.
     * @param integer The user's integer.
     */
    public void setInt(Future<Integer> integer) {
        while (!integer.isDone());

        try {
            this.integer = integer.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert a Response instance to a human friendly string.
     * @return String containing response info.
     */
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

            if (integer != 0) {
                stringBuilder.append("integer:(").append(integer).append(")");
            }

            return stringBuilder.append("]").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "[null]";
    }
}
