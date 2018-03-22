package me.piggypiglet.gary.core.utils.web.papi;

import com.google.gson.JsonArray;
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
    private String json;
    private List<String> placeholders;
    private JsonObject jsonObject;

    public void getJson(String expansion) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://api.extendedclip.com/v3/?name=" + expansion);

        try {
            HttpResponse response = client.execute(get);
            json = EntityUtils.toString(response.getEntity());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        json = "";
    }

    public String loadJson() {
        if (json.equals("")) {
            return "fail - getJson";
        }

        placeholders = new ArrayList<>();
        jsonObject = jsonParser.parse(json).getAsJsonObject();

        if (jsonObject.has("Error")) {
            return "fail - Error";
        }
        return "success";
    }

    public String getAuthor() {
        for (String o : jsonObject.keySet()) {
            JsonObject sub = (JsonObject) jsonObject.get(o);
            if (sub.get("author") != null) {
                return sub.get("author").getAsString();
            }
        }
        return "N/A";
    }

    public String getVersion() {
        for (String o : jsonObject.keySet()) {
            JsonObject sub = (JsonObject) jsonObject.get(o);
            if (sub.get("latest_version") != null) {
                return sub.get("latest_version").getAsString();
            }
        }
        return "";
    }

    public String getPlaceholders() {
        for (String o : jsonObject.keySet()) {
            JsonObject sub = (JsonObject) jsonObject.get(o);
            if (sub.get("placeholders") != null) {
                ((JsonArray) sub.get("placeholders")).forEach(placeholder -> placeholders.add(placeholder.getAsString()));
            }
        }

        return placeholders.toString()
                .replace("]", "")
                .replace("[", "")
                .replace(", ", "\n");
    }
}
