package me.piggypiglet.gary.core.storage.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;
import me.piggypiglet.gary.core.Task;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class FileConfiguration {
    private static final String NULL_STRING = "null";
    private static final int NULL_INT = 0;

    private Map<String, Object> itemMap;
    private Gson gson;

    public FileConfiguration(Map<String, Object> itemMap) {
        this.itemMap = itemMap;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @SuppressWarnings("unchecked")
    public Object get(String path, Object... def) {
        Object object = null;

        if (path.contains(".")) {
            String[] areas = path.split("\\.");
            object = itemMap.getOrDefault(areas[0], null);

            if (areas.length >= 2 && object != null) {
                return getBuriedObject(areas);
            }
        }

        return object == null ? (def.length >= 1 ? def[0] : null) : object;
    }

    public String getString(String path, String... def) {
        Object object;

        try {
            object = get(path, (Object[]) def);
        } catch (Exception e) {
            return NULL_STRING;
        }

        if (object instanceof String) {
            return (String) object;
        }

        return def.length >= 1 ? def[0] : NULL_STRING;
    }

    public int getInt(String path, Integer... def) {
        Object object;

        try {
            object = get(path, (Object[]) def);
        } catch (Exception e) {
            return NULL_INT;
        }

        if (object instanceof Double) {
            return (int) ((double) object);
        }

        return def.length >= 1 ? def[0] : NULL_INT;
    }

    public void setInt(String path) {
        if (path.contains(".")) {
            //todo: code
        }
    }

    // WARNING! Not comment compatible, comments will be removed when using this method
    public void save(File file) {
        try {
            FileUtils.write(file, gson.toJson(gson.toJsonTree(itemMap).getAsJsonObject()), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private Object getBuriedObject(String[] keys) {
        int i = 1;
        Map<String, Object> endObject = (Map<String, Object>) itemMap.get(keys[0]);

        while (instanceOfMap(endObject.get(keys[i]))) {
            endObject = (Map<String, Object>) endObject.get(keys[i++]);
        }

        return endObject.get(keys[i]);
    }

    private boolean instanceOfMap(Object object) {
        if (object instanceof LinkedTreeMap) {
            Set<?> keys = ((LinkedTreeMap<?, ?>) object).keySet();

            return keys.size() >= 1 && keys.toArray()[0] instanceof String;
        }

        return false;
    }
}
