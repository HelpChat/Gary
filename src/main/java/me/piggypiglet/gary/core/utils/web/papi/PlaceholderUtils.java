package me.piggypiglet.gary.core.utils.web.papi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PlaceholderUtils {
    @Inject private JsonParser jsonParser;

    private String getJson(String expansion) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://api.extendedclip.com/v3/?name=" + expansion);

        try {
            HttpResponse response = client.execute(get);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    return "";
    }

    @SuppressWarnings("unchecked")
    public String getPlaceholders(String expansion) {
        List<String> placeholders = new ArrayList<>();
        JsonObject jsonObject = jsonParser.parse(getJson(expansion)).getAsJsonObject();

        if (jsonObject.has("Error")) {
            return "fail";
        }

        for (String o : jsonObject.keySet()) {
            JsonObject sub = (JsonObject) jsonObject.get(o);
            if (sub.get("placeholders") != null && sub.get("placeholders") instanceof List) {
                placeholders = (List<String>) sub.get("placeholders");
            }
        }
        return placeholders.toString()
                .replace("]", "")
                .replace("[", "")
                .replace(", ", "\n");
    }
}
