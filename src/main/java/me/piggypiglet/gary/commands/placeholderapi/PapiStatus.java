package me.piggypiglet.gary.commands.placeholderapi;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.storage.file.Lang;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.net.HttpURLConnection;
import java.net.URL;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PapiStatus extends Command {
    public PapiStatus() {
        super("papi status");
        options.setDescription("Get the current status of the eCloud (online/offline).");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://api.extendedclip.com/").openConnection();

            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                e.getChannel().sendMessage(Lang.getString("commands.papi.status.failure")).queue();
            } else {
                e.getChannel().sendMessage(Lang.getString("commands.papi.status.success")).queue();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
