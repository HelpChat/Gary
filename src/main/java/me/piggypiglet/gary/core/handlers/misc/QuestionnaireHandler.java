package me.piggypiglet.gary.core.handlers.misc;

import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class QuestionnaireHandler {
    @Getter private final Map<String, QuestionnaireBuilder> questionnaires = new ConcurrentHashMap<>();
}
