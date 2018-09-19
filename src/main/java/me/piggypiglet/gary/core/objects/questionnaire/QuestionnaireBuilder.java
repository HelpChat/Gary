package me.piggypiglet.gary.core.objects.questionnaire;

import emoji4j.EmojiUtils;
import lombok.Getter;
import me.piggypiglet.gary.core.utils.discord.EventUtils;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class QuestionnaireBuilder {
    private Map<String, Question> questions;
    private Map<String, Response> responses;
    private final Member member;
    private final TextChannel channel;

    public QuestionnaireBuilder(@Nonnull Member member, @Nonnull TextChannel channel) {
        questions = new LinkedHashMap<>();
        responses = new HashMap<>();
        this.member = member;
        this.channel = channel;
    }

    public QuestionnaireBuilder clear(boolean responses) {
        questions.clear();
        if (responses) this.responses.clear();

        return this;
    }

    public QuestionnaireBuilder addQuestions(Question... questions) {
        Arrays.asList(questions).forEach(question -> this.questions.put(question.getKey(), question));

        return this;
    }

    public QuestionnaireBuilder addResponses(Response... responses) {
        Arrays.asList(responses).forEach(response -> this.responses.put(response.getKey(), response));

        return this;
    }

    public QuestionnaireBuilder setQuestion(String key, boolean removeResponse, Question newQuestion) {
        if (removeResponse) responses.remove(key);

        questions.put(key, newQuestion);

        return this;
    }

    public QuestionnaireBuilder setResponse(String key, Response newResponse) {
        responses.put(key, newResponse);

        return this;
    }

    public QuestionnaireBuilder removeQuestions(boolean removeResponses, String... keys) {
        Arrays.asList(keys).forEach(key -> {
            questions.remove(key);
            if (removeResponses) responses.remove(key);
        });

        return this;
    }

    public Questionnaire build() {
        questions.values().forEach(question -> {
            Object[] acceptableAnswers = question.getAcceptableAnswers();
            Message message = channel.sendMessage(question.getQuestion()).complete();
            final AtomicBoolean continueOn = new AtomicBoolean(true);
            final AtomicBoolean stringInstance = new AtomicBoolean(false);
            Response response = new Response(question.getKey());

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
                response.setMessage(EventUtils.pullMessage(channel, member.getUser()));
            } else {
                response.setReaction(EventUtils.pullReaction(message, member.getUser()));
            }

            try {
                responses.put(question.getKey(), response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return new Questionnaire(questions, responses, this);
    }

    public final class Questionnaire {
        @Getter private Map<String, Question> questions;
        @Getter private Map<String, Response> responses;
        @Getter private QuestionnaireBuilder builder;

        public Questionnaire() throws InstantiationException {
            throw new InstantiationException("This class cannot be instantiated.");
        }

        private Questionnaire(@Nonnull Map<String, Question> questions, @Nonnull Map<String, Response> responses, @Nonnull QuestionnaireBuilder builder) {
            this.questions = questions;
            this.responses = responses;
            this.builder = builder;
        }
    }
}
