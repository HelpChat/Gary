package me.piggypiglet.gary.commands.implementations.spigot;

import be.maximvdw.spigotsite.SpigotSiteCore;
import be.maximvdw.spigotsite.api.SpigotSiteAPI;
import be.maximvdw.spigotsite.api.user.User;
import com.google.inject.Inject;
import me.piggypiglet.gary.commands.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class TestCommand extends Command {
    @Inject private SpigotSiteAPI spigot;
    @Inject private User me;

    public TestCommand() {
        super("test");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        try {
            e.getChannel().sendMessage(spigot.getUserManager().getUserById(266605).getUsername()).queue();
            e.getChannel().sendMessage(me.getUsername()).queue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
