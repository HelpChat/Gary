package me.piggypiglet.gary.utils;

import me.piggypiglet.gary.conversation.Prompt;

import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class ConversationUtils {
    public static Prompt.PromptBuilder multiChoice(List<String> options) {
        return multiChoice("Please choose one of the below (type it's number):", options);
    }

    public static Prompt.PromptBuilder multiChoice(String question, List<String> options) {
        StringBuilder str = new StringBuilder(question.endsWith("\n\n") ? question : question + "\n\n");

        for (int i = 0; i < options.size(); i++) {
            str.append(i + 1).append(" - ").append(options.get(i)).append("\n");
        }

        return Prompt.builder().question(str.toString()).match(s -> {
            try {
                int i = Integer.parseInt(s);
                return i <= options.size();
            } catch (Exception e) {
                return false;
            }
        });
    }
}
