package me.piggypiglet.gary;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.tasks.WordChanger;

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
public final class ChatReaction {
    @Inject private GFile files;
    @Inject private WordChanger wordChanger;
    private File wordsFile;

    void loadWords() {
        wordsFile = files.getFile("words");
    }

    public List<String> getWords() {
        if (wordsFile == null) {
            loadWords();
        }

        try {
            List<String> words = new ArrayList<>();
            Files.lines(Paths.get(wordsFile.getPath())).forEach(words::add);
            String wordsString = words.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", " ");
            return Arrays.asList(wordsString.split(" "));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void generateNewWord() {
        wordChanger.run();
    }

    public String getCurrentWord() {
        return wordChanger.getCurrentWord();
    }
}
