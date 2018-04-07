package me.piggypiglet.gary.core.objects;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.utils.file.ConfigUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class GFile {
    @Inject private ConfigUtils cutil;
    private Map<String, File> gFiles;

    public void make(String name, String externalPath, String internalPath) {
        if (gFiles == null) {
            gFiles = new HashMap<>();
        }

        File file = new File(externalPath);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (file.createNewFile()) {
                    if (cutil.exportResource(GaryBot.class.getResourceAsStream(internalPath), externalPath)) {
                        System.out.println(name + " successfully created.");
                        gFiles.put(name, file);
                    } else {
                        System.out.println(name + " creation failed.");
                    }
                }
            } else {
                System.out.println(name + " successfully loaded.");
                gFiles.put(name, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile(String fileName) {
        return gFiles.get(fileName);
    }

    public String getItem(String fileName, String item) {
        File file = gFiles.get(fileName);
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(file));
            Map<String, String> data = gson.fromJson(reader, LinkedTreeMap.class);
            if (data.containsKey(item)) {
                return data.get(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item + " not found in " + fileName;
    }

    public Map<String, File> getgFiles() {
        return gFiles;
    }

    // TODO: Change this to a better solution
    public void setWord(String currentWord) {
        Gson gson = new Gson();
        String json = "{\"current-word\": " + gson.toJson(currentWord) + "}";
        try {
            FileWriter writer = new FileWriter(gFiles.get("word-storage"));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
