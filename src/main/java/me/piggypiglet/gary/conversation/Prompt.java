package me.piggypiglet.gary.conversation;

import lombok.Builder;
import lombok.Data;

import java.util.function.Predicate;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@Data @Builder
public final class Prompt {
    private final String key;
    private final Predicate<String> match;
    private final String question;
    private final boolean isEnd;
}
