package me.piggypiglet.gary.core.objects.questionnaire;

import lombok.Getter;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Question {
    @Getter private String question;
    @Getter private Object[] acceptableAnswers;

    public Question(String question, Object... acceptableAnswers) {
        this.question = question;
        this.acceptableAnswers = acceptableAnswers;
    }
}
