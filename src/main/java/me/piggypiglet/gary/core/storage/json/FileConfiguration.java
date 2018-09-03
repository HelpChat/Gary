package me.piggypiglet.gary.core.storage.json;

import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;
import me.piggypiglet.gary.core.Task;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class FileConfiguration {
    private Map<String, Object> itemMap;

    public FileConfiguration(Map<String, Object> itemMap) {
        this.itemMap = itemMap;
    }

    @SuppressWarnings("unchecked")
    public Object get(String path, Object... def) {
        Object object = itemMap.getOrDefault(path, null);

        if (path.contains(".") && object != null) {
            String[] areas = path.split("\\.");

            if (areas.length >= 2) {
                int i = 1;
                Map<String, Object> endObject = (Map<String, Object>) itemMap.get(areas[0]);

                while (instanceOfMap(endObject.get(areas[i]))) {
                    endObject = (Map<String, Object>) endObject.get(areas[i++]);
                }

                return endObject.get(areas[i]);
            }
        }

        return object == null ? (def.length >= 1 ? def[0] : null) : object;
    }

    private boolean instanceOfMap(Object object) {
        if (object instanceof LinkedTreeMap) {
            Set<?> keys = ((LinkedTreeMap<?, ?>) object).keySet();

            return keys.size() >= 1 && keys.toArray()[0] instanceof String;
        }

        return false;
    }
}
