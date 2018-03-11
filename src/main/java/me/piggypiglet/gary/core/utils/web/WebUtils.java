package me.piggypiglet.gary.core.utils.web;

import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class WebUtils {

    @Inject private JsonParser jsonParser;

    public String hastebin(String str) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://hastebin.com/documents");

        try {
            post.setEntity(new StringEntity(str, "UTF-8"));
            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            return "https://hastebin.com/raw/" + jsonParser.parse(result).getAsJsonObject().get("key").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

}
