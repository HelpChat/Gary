package me.piggypiglet.gary.commands.admin.database;

import co.aikar.idb.DB;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.storage.Users;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class SyncUsers extends Command {
    @Inject private Users users;

    public SyncUsers() {
        super("?syncusers");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (e.getMember().getUser().getIdLong() == Constants.PIGGYPIGLET) {
            List<Long> members = new ArrayList<>();
            e.getGuild().getMembers().forEach(member -> members.add(member.getUser().getIdLong()));
            List<Long> database = new ArrayList<>();

            try {
                DB.getResultsAsync("SELECT * FROM `gary_users`").get().forEach(member -> database.add(member.getLong("discord_id")));
                database.forEach(members::remove);

                members.forEach(member -> {
                    try {
                        users.addUser(e.getJDA().getUserById(member));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                String users = members.size() >= 2 || members.size() == 0 ? members.size() + " users have" : "1 user has";
                e.getChannel().sendMessage(users + " been added to the database.").queue();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
