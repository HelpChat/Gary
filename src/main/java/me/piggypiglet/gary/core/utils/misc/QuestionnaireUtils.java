package me.piggypiglet.gary.core.utils.misc;

import me.piggypiglet.gary.core.objects.enums.QuestionType;
import me.piggypiglet.gary.core.objects.questionnaire.Question;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class QuestionnaireUtils {
    public static String askQuestion(String question, Member member, TextChannel channel) {
        return new QuestionnaireBuilder(member, channel).addQuestions(
                new Question("q", question, QuestionType.STRING)
        ).build("temp").getResponses().get("q").getMessage().getContentRaw();
    }
}
