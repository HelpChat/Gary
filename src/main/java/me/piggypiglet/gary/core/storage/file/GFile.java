package me.piggypiglet.gary.core.storage.file;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.GaryBootstrap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class GFile {
    @Inject private me.piggypiglet.gary.core.utils.file.FileUtils fileUtils;
    @Inject private Gson gson;

    @Getter private Map<String, Map<String, Object>> itemMaps;
    private Logger logger;

    public GFile() {
        itemMaps = new ConcurrentHashMap<>();
        logger = LoggerFactory.getLogger("GFile");
    }

    /**
     * Generates a physical file and populates it with embedded content.
     * @param name The name of the file, will be stored and accessible via a map.
     * @param externalPath Path the file will be generated at.
     * @param internalPath Path to the embedded content.
     */
    public void make(String name, String externalPath, String internalPath, boolean embedded) {
        name = name.substring(0, name.lastIndexOf('.'));

        if (!embedded) {
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
        } else {
            InputStream stream = GaryBootstrap.class.getResourceAsStream(internalPath);

            try {
                insertDirectText(name, IOUtils.toString(stream, StandardCharsets.UTF_8), isJson(internalPath));
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.info(name + " successfully loaded.");
        }
    }

    private boolean isJson(String path) {
        return path.toLowerCase().endsWith(".json");
    }

    @SuppressWarnings("unchecked")
    private void insertIntoMap(String name, File file) {
        Map<String, Object> populatorMap = new HashMap<>();

        try {
            String fileContent = FileUtils.readFileToString(file, "UTF-8");

            if (isJson(file.getPath())) {
                populatorMap.put("file-configuration", new FileConfiguration(gson.fromJson(fileContent, LinkedTreeMap.class)));
            } else {
                populatorMap.put("file-content", fileContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        populatorMap.put("file", file);
        itemMaps.put(name, populatorMap);
    }

    @SuppressWarnings("unchecked")
    private void insertDirectText(String name, String text, boolean json) {
        Map<String, Object> popularMap = new HashMap<>();

        if (json) {
            popularMap.put("file-configuration", new FileConfiguration(gson.fromJson(text, LinkedTreeMap.class)));
        } else {
            popularMap.put("file-content", text);
        }

        itemMaps.put(name, popularMap);
    }

    /**
     * Get a FileConfiguration instance for the GFile specified.
     * @param name The name of the GFile to be retrieved.
     * @return Returns a FileConfiguration instance of the referenced GFile.
     */
    public FileConfiguration getFileConfiguration(String name) {
        Object item = itemMaps.get(name).get("file-configuration");

        if (item instanceof FileConfiguration) {
            return (FileConfiguration) item;
        }

        return new FileConfiguration(new HashMap<>());
    }
}
