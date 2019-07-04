package me.piggypiglet.gary.pterodactyl.objects;

import com.stanjg.ptero4j.entities.panel.user.UserServer;
import lombok.Data;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@Data
public final class Server {
    private final com.stanjg.ptero4j.entities.panel.admin.Server adminServer;
    private final UserServer userServer;
    private String name;
    private String id;

    public Server(com.stanjg.ptero4j.entities.panel.admin.Server adminServer, UserServer userServer) {
        this.adminServer = adminServer;
        this.userServer = userServer;
        name = adminServer.getName();
        id = userServer.getId();
    }
}
