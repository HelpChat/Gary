package me.piggypiglet.gary.core.objects.questionnaire;

import lombok.Getter;
import me.piggypiglet.gary.core.objects.enums.QuestionType;
import me.piggypiglet.gary.core.objects.tasks.Task;
import me.piggypiglet.gary.core.utils.discord.EventUtils;
import net.dv8tion.jda.core.entities.*;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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

    public Questionnaire build(@Nonnull String questionnaireName) {
        CompletableFuture<Questionnaire> questionnaire = new CompletableFuture<>();

        Task.async(r -> {
            questions.values().forEach(question -> {
                List<Object> emotes = question.getEmotes();
                Message message = channel.sendMessage(question.getQuestion()).complete();
                QuestionType questionType = question.getQuestionType();
                Response response = new Response(question.getKey());

                if (emotes != null && questionType == QuestionType.EMOTE) {
                    emotes.forEach(e -> {
                        if (e instanceof String) {
                            message.addReaction((String) e).queue();
                        }

                        if (e instanceof Emote) {
                            message.addReaction((Emote) e).queue();
                        }
                    });
                } else if (questionType == QuestionType.EMOTE) {
                    channel.sendMessage("Whoever made this questionnaire fucked up and forgot to add acceptable answers for this question. I'm cancelling this questionnaire.").queue();
                    channel.getGuild().getTextChannelById(411094432402636802L).sendMessage("Whoever made the questionnaire: `" + questionnaireName + "` forgot to add acceptable emotes for question: `" + question.getKey() + "`. Please fix this ASAP.").queue();
                    return;
                }

                User user = member.getUser();

                switch (questionType) {
                    case STRING:
                        response.setMessage(EventUtils.pullMessage(channel, user));
                        break;

                    case EMOTE:
                        response.setReaction(EventUtils.pullReaction(message, user));
                        break;

                    case INT:
                        response.setInt(EventUtils.pullInt(channel, user));
                        break;
                }

                responses.put(question.getKey(), response);
            });

            questionnaire.complete(new Questionnaire(questions, responses, QuestionnaireBuilder.this));
        });

        //noinspection StatementWithEmptyBody
        while (!questionnaire.isDone()) {}

        try {
            return questionnaire.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Questionnaire(questions, new HashMap<>(), this);
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
