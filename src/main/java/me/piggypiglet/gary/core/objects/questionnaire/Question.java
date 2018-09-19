package me.piggypiglet.gary.core.objects.questionnaire;

import lombok.Getter;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Question {
    @Getter private String key;
    @Getter private String question;
    @Getter private Object[] acceptableAnswers;

    public Question(String key, String question, Object... acceptableAnswers) {
        this.key = key;
        this.question = question;
        this.acceptableAnswers = acceptableAnswers;
    }
}
