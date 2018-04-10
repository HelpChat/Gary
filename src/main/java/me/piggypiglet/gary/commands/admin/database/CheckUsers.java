package me.piggypiglet.gary.commands.admin.database;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class CheckUsers extends Command {
    public CheckUsers() {
        super("?checkusers", "", false);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (e.getMember().getUser().getIdLong() == Constants.PIGGYPIGLET) {
            List<Long> members = new ArrayList<>();
            e.getGuild().getMembers().forEach(member -> members.add(member.getUser().getIdLong()));
            List<Long> dbMembers = new ArrayList<>();

            try {
                List<DbRow> rows = DB.getResultsAsync("SELECT * FROM `gary_users`").get();
                rows.forEach(id -> dbMembers.add(id.getLong("discord_id")));

                if (!dbMembers.containsAll(members)) {
                    e.getChannel().sendMessage("The database is not synced.").queue();
                } else {
                    e.getChannel().sendMessage("The database is synced.").queue();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
