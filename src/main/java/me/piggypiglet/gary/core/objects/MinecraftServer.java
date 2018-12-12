package me.piggypiglet.gary.core.objects;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import me.piggypiglet.gary.core.storage.file.FileConfiguration;
import me.piggypiglet.gary.core.utils.http.WebUtils;

import javax.annotation.Nonnull;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MinecraftServer {
    @Getter private final String ip;
    @Getter private boolean success = true;
    @Getter private String motd;
    @Getter private String favicon;
    @Getter private String version;
    @Getter private boolean premium;

    @SuppressWarnings("unchecked")
    public MinecraftServer(@Nonnull final String ip) {
        this.ip = ip;

        String url = "https://mcapi.us/server/status?ip=" + (ip.contains(":") ? ip.split(":")[0] + "&port=" + ip.split(":")[1] : ip);
        String response = WebUtils.getStringEntity(url);
        FileConfiguration server;

        if (!response.isEmpty() && !response.contains("status\":\"error")) {
             server = new FileConfiguration(new Gson().fromJson(WebUtils.getStringEntity(url), LinkedTreeMap.class));
        } else {
            success = false;
            return;
        }

        motd = server.getString("motd");
        premium = server.getBoolean("online");
        favicon = server.getString("favicon");
        version = server.getString("server.name");
    }
}
