package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.storage.json.GTypes;
import me.piggypiglet.gary.core.tasks.WordChanger;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class ChatReaction {
    @Inject private GTypes gTypes;
    @Inject private WordChanger wordChanger;

    private List<String> words;

    void loadWords() {
        words = Arrays.asList(gTypes.getString("words", "file-content").split(" "));
    }

    public List<String> getWords() {
        return this.words;
    }

    public void generateNewWord() {
        wordChanger.run();
    }

    public Message getCurrentMessage() {
        return wordChanger.getCurrentMessage();
    }

    public void setWord(String word) {
        this.words = new ArrayList<>();
        this.words.add(word);
        generateNewWord();
        loadWords();
    }

    public String getCurrentWord() {
        return wordChanger.getCurrentWord();
    }
}
