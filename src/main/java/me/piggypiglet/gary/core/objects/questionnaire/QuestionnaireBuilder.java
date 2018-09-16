package me.piggypiglet.gary.core.objects.questionnaire;

import emoji4j.EmojiUtils;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class QuestionnaireBuilder {
    @Getter private List<Question> questions;

    public QuestionnaireBuilder() {
        questions = new ArrayList<>();
    }

    public void addQuestions(Question... questions) {
        this.questions.addAll(Arrays.asList(questions));
    }

    public void build(Member member, TextChannel channel) {
        questions.forEach(question -> {
            Object[] acceptableAnswers = question.getAcceptableAnswers();
            boolean unicodeEmotes = acceptableAnswers instanceof String[] && Arrays.stream((String[]) acceptableAnswers).allMatch(EmojiUtils::isEmoji);

            if (unicodeEmotes || acceptableAnswers instanceof Emote[]) {

            }
        });
    }
}
