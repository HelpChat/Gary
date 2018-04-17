package me.piggypiglet.gary;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.tasks.WordChanger;
import net.dv8tion.jda.core.entities.Message;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class ChatReaction {
    @Inject private GFile files;
    @Inject private WordChanger wordChanger;

    private List<String> words;

    void loadWords() {
        File wordsFile = files.getFile("words");

        try {
            List<String> words = new ArrayList<>();
            Files.lines(Paths.get(wordsFile.getPath())).forEach(words::add);
            String wordsString = words.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", " ");

            this.words = Arrays.asList(wordsString.split(" "));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
