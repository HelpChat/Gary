package me.piggypiglet.gary.commands.implementations;

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
public final class TestCommand extends Command {
    @Inject private PteroAdminAPI pteroAdminAPI;
    @Inject private PteroUserAPI pteroUserAPI;

    public TestCommand() {
        super("test");
        options.setPermission("test");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            List<Server> servers = pteroAdminAPI.getServersController().getServers(args[0]);

            if (servers.size() > 0) {
                System.out.println(servers.get(0).getUuid());
                System.out.println(pteroUserAPI.getServersController().getServers().get(0).getId());
            } else {
                e.getChannel().sendMessage("no servers found lol").queue();
            }
        }
    }
}
