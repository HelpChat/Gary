package me.piggypiglet.gary.core.utils.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class WebUtils {
    public static String getStringEntity(String URL) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(URL);

        try {
            HttpResponse response = client.execute(get);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
