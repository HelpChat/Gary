package me.piggypiglet.gary.core.utils.http;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.storage.file.FileConfiguration;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class HasteUtils {
    @Inject private static GaryBot garyBot;
    @Inject private static Gson gson;

    /**
     * Upload a string to hastebin.
     * @param str The string that is uploaded to hastebin.
     * @return Returns the link to the hastebin containing the parameter value.
     */
    @SuppressWarnings("unchecked")
    public static String haste(String str) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://paste.helpch.at/documents");

        try {
            post.setEntity(new StringEntity(str));

            return "https://paste.helpch.at/" + new FileConfiguration(gson.fromJson(EntityUtils.toString(client.execute(post).getEntity()), LinkedTreeMap.class)).getString("key", "null");
        } catch (Exception e) {
            garyBot.getJda().getTextChannelById(Constants.GARY).sendMessage("<@" + Constants.PIGGYPIGLET + "> yo pig we fucked up, paste is down.").queue();
            post = new HttpPost("https://hastebin.com/documents");

            try {
                post.setEntity(new StringEntity(str));

                return "https://hastebin.com/" + new FileConfiguration(gson.fromJson(EntityUtils.toString(client.execute(post).getEntity()), LinkedTreeMap.class)).getString("key", "null");
            } catch (Exception ex) {
                garyBot.getJda().getTextChannelById(Constants.GARY).sendMessage("and haste is also down, maybe something is messed in gary. I'll print a stack trace.").queue();
                ex.printStackTrace();
            }
        }

        return "";
    }
}
