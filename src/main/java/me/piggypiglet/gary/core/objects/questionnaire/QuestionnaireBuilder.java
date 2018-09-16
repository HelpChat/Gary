package me.piggypiglet.gary.core.objects.questionnaire;

import emoji4j.EmojiUtils;
import lombok.Getter;
import me.piggypiglet.gary.core.utils.discord.EventUtils;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class QuestionnaireBuilder {
    @Getter private List<Question> questions;
    @Getter private List<Response> responses;

    public QuestionnaireBuilder() {
        questions = new ArrayList<>();
    }

    public QuestionnaireBuilder addQuestions(Question... questions) {
        this.questions.addAll(Arrays.asList(questions));

        return this;
    }

    public void build(Member member, TextChannel channel) {
        questions.forEach(question -> {
            Object[] acceptableAnswers = question.getAcceptableAnswers();
            Message message = channel.sendMessage(question.getQuestion()).complete();
            final AtomicBoolean continueOn = new AtomicBoolean(true);
            final AtomicBoolean stringInstance = new AtomicBoolean(false);
            CompletableFuture<Response> response = new CompletableFuture<>();

            System.out.println("int");

            Arrays.stream(acceptableAnswers).forEach(acceptableAnswer -> {
                if (acceptableAnswer instanceof String) stringInstance.set(true);

                if (acceptableAnswer instanceof String && EmojiUtils.isEmoji((String) acceptableAnswer)) {
                    message.addReaction((String) acceptableAnswer).queue();

                    continueOn.set(false);
                }

                if (acceptableAnswer instanceof Emote) {
                    message.addReaction((Emote) acceptableAnswer).queue();

                    continueOn.set(false);
                }
            });

            if (continueOn.get() && stringInstance.get()) {
                response.complete(new Response().setMessage(EventUtils.pullMessage(channel, member.getUser())));
            } else {
                response.complete(new Response().setReactionEmote(EventUtils.pullReaction(message, member.getUser())));
            }

            try {
                System.out.println(response.get().toString());
                responses.add(response.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
