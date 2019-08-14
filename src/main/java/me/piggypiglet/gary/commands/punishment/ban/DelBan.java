package me.piggypiglet.gary.commands.punishment.ban;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.storage.file.Lang;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class DelBan extends Command {
    public DelBan() {
        super("unban");
        options.setRole(Roles.TRUSTED).setDescription("Unban a user.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            e.getGuild().unban(args[0]).queue();
            e.getChannel().sendMessage(Lang.getString("commands.punishment.ban.del.success", args[0])).queue();
        } else {
            e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "unban <user id>")).queue();
        }
    }
}
