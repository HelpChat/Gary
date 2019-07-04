package me.piggypiglet.gary.pterodactyl;

import com.google.inject.Inject;
import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.PteroUserAPI;
import me.piggypiglet.gary.pterodactyl.objects.Server;

import java.util.List;
import java.util.stream.Collectors;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class Pterodactyl {
    @Inject private PteroAdminAPI pteroAdminAPI;
    @Inject private PteroUserAPI pteroUserAPI;

    public List<Server> getServers(String name) {
        System.out.println(pteroAdminAPI.getServersController().getServers(name));
        return pteroAdminAPI.getServersController().getServers(name).stream()
                .map(s -> new Server(s, pteroUserAPI.getServersController().getServer(s.getUuid().split("-")[0])))
                .collect(Collectors.toList());
    }
}