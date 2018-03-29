package me.piggypiglet.gary.core.utils.mc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServerInfoUtils {
    @Inject private JsonParser jsonParser;

    public boolean serverStatus(String ip, String port) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://use.gameapis.net/mc/query/status/" + ip + ":" + port);

        try {
            HttpResponse response = client.execute(get);
            String result = EntityUtils.toString(response.getEntity());
            return jsonParser.parse(result).getAsJsonObject().get("status").getAsBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getIconURL(String ip, String port) {
        if (serverStatus(ip, port)) {
            return "https://use.gameapis.net/mc/query/icon/" + ip + ":" + port;
        }
        return "https://use.gameapis.net/mc/query/icon/testplugins.com:25565";
    }

    public String getInfo(String info, String ip, String port) {
        if (serverStatus(ip, port)) {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet("https://use.gameapis.net/mc/query/info/" + ip + ":" + port);

            try {
                HttpResponse response = client.execute(get);
                String result = EntityUtils.toString(response.getEntity());
                JsonObject object = jsonParser.parse(result).getAsJsonObject();
                if (info.equalsIgnoreCase("motd")) {
                    return object.get("motds").getAsJsonObject().get("ingame").getAsString();
                } else if (object.has(info)) {
                    return object.get(info).getAsString();
                }
                return "N/A";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "N/A";
    }
}
