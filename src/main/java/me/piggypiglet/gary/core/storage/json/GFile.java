package me.piggypiglet.gary.core.storage.json;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.utils.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class GFile {
    @Inject private FileUtils fileUtils;
    @Inject private Gson gson;

    private Map<String, Map<String, Object>> itemMaps;
    private Logger logger;

    public GFile() {
        itemMaps = new HashMap<>();
        logger = LoggerFactory.getLogger("GFile");
    }

    public void make(String name, String externalPath, String internalPath) {
        File file = new File(externalPath);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (file.createNewFile()) {
                    if (fileUtils.exportResource(GaryBot.class.getResourceAsStream(internalPath), externalPath)) {
                        insertIntoMap(name, file);

                        logger.info(name + " successfully created.");
                    } else {
                        logger.error(name + " creation failed.");
                    }
                }
            } else {
                insertIntoMap(name, file);

                logger.info(name + " successfully loaded.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isJson(File file) {
        try {
            String fileContent = org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
            String path = file.getPath();

            return path.substring(path.lastIndexOf(".") + 1, path.length()).equalsIgnoreCase("json") &&
                    fileContent.startsWith("{") &&
                    fileContent.endsWith("}");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private void insertIntoMap(String name, File file) {
        itemMaps.put(name, new HashMap<>());

        try {
            String fileContent = org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");

            if (isJson(file)) {
                itemMaps.put(name, gson.fromJson(fileContent, LinkedTreeMap.class));
            } else {
                itemMaps.get(name).put("file-content", fileContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        itemMaps.get(name).put("file", file);
    }

    Object getItem(String fileName, String item) {
        return itemMaps.get(fileName).get(item);
    }

    public Map<String, Map<String, Object>> getItemMaps() {
        return itemMaps;
    }
}
