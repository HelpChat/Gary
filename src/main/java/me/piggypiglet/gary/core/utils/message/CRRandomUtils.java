package me.piggypiglet.gary.core.utils.message;

import com.google.common.primitives.Chars;
import com.google.inject.Inject;
import me.piggypiglet.gary.ChatReaction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class CRRandomUtils {
    @Inject private ChatReaction cr;

    public String getRandomWord() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomNumber = random.nextInt(Objects.requireNonNull(cr.getWords()).size());
        return cr.getWords().get(randomNumber);
    }

    public String scrambleWord(String word) {
        List<Character> chars = Chars.asList(word.toCharArray());
        Collections.shuffle(chars);
        return new String(Chars.toArray(chars));
    }
}
