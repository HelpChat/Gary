package me.piggypiglet.gary.core.objects.placeholderapi;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import me.piggypiglet.gary.core.storage.file.FileConfiguration;
import me.piggypiglet.gary.core.utils.http.WebUtils;
import me.piggypiglet.gary.core.utils.string.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PapiExpansion {
    private final Gson gson;
    private FileConfiguration json;
    @Getter private boolean success = false;

    public PapiExpansion(Gson gson) {
        this.gson = gson;
    }

    @SuppressWarnings("unchecked")
    public PapiExpansion load(String expansionName) {
        String response = WebUtils.getStringEntity("https://api.extendedclip.com/v3/?name=" + expansionName);

        if (!response.isEmpty()) {
            Map<String, Object> itemMap = gson.fromJson(response, LinkedTreeMap.class);
            json = new FileConfiguration((Map<String, Object>) itemMap.get(itemMap.keySet().toArray(new String[]{})[0]));
            success = true;
        }

        return this;
    }

    public String getAuthor() {
        return json.getString("author", "clip");
    }

    public List<String> getPlaceholders() {
        return Arrays.asList(String.join("\n", StringUtils.replaceAll(json.getStringList("placeholders"), "_", "\\_")).replaceAll("((.*\\s*\\n\\s*){15})", "$1-SEPARATOR-\n").split("-SEPARATOR-"));
    }

    public String getVersion() {
        return json.getString("latest_version", "1.0");
    }
}
