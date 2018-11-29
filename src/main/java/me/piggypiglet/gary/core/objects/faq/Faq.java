package me.piggypiglet.gary.core.objects.faq;

import lombok.Data;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Data
public final class Faq {
    private final String key;
    private final String faq;
    private final long authorId;
}
