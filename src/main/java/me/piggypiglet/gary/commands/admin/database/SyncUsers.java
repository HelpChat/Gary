package me.piggypiglet.gary.commands.admin.database;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import me.piggypiglet.gary.core.utils.mysql.UserUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Objects;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class SyncUsers extends Command {
    @Inject private RoleUtils roleUtils;
    @Inject private UserUtils userUtils;

    public SyncUsers() {
        super("?syncusers", "", false);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (e.getAuthor().getIdLong() == Constants.PIGGYPIGLET) {
            e.getChannel().sendMessage(Objects.requireNonNull(userUtils.syncUsers(e.getGuild(), e.getJDA()))).queue();
        }
    }
}
