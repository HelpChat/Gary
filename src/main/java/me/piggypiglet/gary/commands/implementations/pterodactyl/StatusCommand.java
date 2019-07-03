package me.piggypiglet.gary.commands.implementations.pterodactyl;

import com.google.inject.Inject;
import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.panel.admin.Server;
import me.piggypiglet.gary.commands.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class StatusCommand extends Command {
    @Inject private PteroAdminAPI pteroAdminAPI;
    @Inject private PteroUserAPI pteroUserAPI;

    public StatusCommand() {
        super("ptero status", "pterodactyl status");
        options.setPermission("pterodactyl.status");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            List<Server> servers = pteroAdminAPI.getServersController().getServers(args[0]);

            if (servers.size() > 0) {
                e.getChannel().sendMessage(pteroUserAPI.getServersController().getServer(servers.get(0).getUuid().split("-")[0]).getPowerState().getValue()).queue();
            } else {
                e.getChannel().sendMessage("no servers found lol").queue();
            }
        }
    }
}
