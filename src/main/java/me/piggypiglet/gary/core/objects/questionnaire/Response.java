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
    @Getter private Future<MessageReaction.ReactionEmote> reactionEmote;
    @Getter private Future<Message> message;

    public Response setReactionEmote(Future<MessageReaction.ReactionEmote> reactionEmote) {
        this.reactionEmote = reactionEmote;
        return this;
    }

    public Response setMessage(Future<Message> message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        try {
            StringBuilder stringBuilder = new StringBuilder();

            if (reactionEmote != null && reactionEmote.isDone()) {
                stringBuilder.append("unicode: ").append(reactionEmote.get()).append(", ");
            }

            if (message != null && message.isDone()) {
                stringBuilder.append("message: ").append(message.get().getContentRaw());
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "null";
    }
}
