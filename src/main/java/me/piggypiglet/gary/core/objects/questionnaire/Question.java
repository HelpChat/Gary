package me.piggypiglet.gary.core.objects.questionnaire;

import lombok.Getter;
import lombok.Setter;
import me.piggypiglet.gary.core.objects.enums.QuestionType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Question {
    @Getter private String key;
    @Getter private String question;
    @Getter private QuestionType questionType;
    @Setter private Object[] emotes;

    public Question(String key, String question, QuestionType questionType) {
        this.key = key;
        this.question = question;
        this.questionType = questionType;
    }

    public String[] getUnicodes() {
        if (emotes instanceof String[]) {
            return (String[]) emotes;
        }

        return null;
    }

    public Emote[] getEmotes(JDA jda) {
        if (emotes instanceof Emote[]) {

        }
    }
}
