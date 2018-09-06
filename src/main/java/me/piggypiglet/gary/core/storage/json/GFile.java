package me.piggypiglet.gary.core.storage.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.GaryBootstrap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class GFile {
    @Inject private me.piggypiglet.gary.core.utils.file.FileUtils fileUtils;
    @Inject private Gson gson;

    private Map<String, Map<String, Object>> itemMaps;
    private Logger logger;

    public GFile() {
        itemMaps = new HashMap<>();
        logger = LoggerFactory.getLogger("GFile");
    }

    public Map<String, Map<String, Object>> getItemMaps() {
        return itemMaps;
    }

    public void make(String name, String externalPath, String internalPath) {
        File file = new File(externalPath);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();

                if (file.createNewFile()) {
                    if (fileUtils.exportResource(GaryBootstrap.class.getResourceAsStream(internalPath), externalPath)) {
                        insertIntoMap(name, file);

                        logger.info(name + " successfully created & loaded.");
                    } else {
                        logger.error(name + " creation failed.");
                    }
                } else {
                    logger.error(name + " creation failed.");
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
        return file.getPath().endsWith(".json");
    }

    @SuppressWarnings("unchecked")
    private void insertIntoMap(String name, File file) {
        // We need to populate the map in-case the file isn't json.
        Map<String, Object> tempMap = new HashMap<>();
        itemMaps.put(name, tempMap);

        try {
            String fileContent = FileUtils.readFileToString(file, "UTF-8");

            if (isJson(file)) {
                itemMaps.get(name).put("file-configuration", new FileConfiguration(gson.fromJson(fileContent, LinkedTreeMap.class)));
            } else {
                itemMaps.get(name).put("file-content", fileContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        itemMaps.get(name).put("file", file);
    }

    public FileConfiguration getFileConfiguration(String name) {
        Object item = itemMaps.get(name).get("file-configuration");

        if (item instanceof FileConfiguration) {
            return (FileConfiguration) item;
        }

        return new FileConfiguration(new HashMap<>());
    }
}
