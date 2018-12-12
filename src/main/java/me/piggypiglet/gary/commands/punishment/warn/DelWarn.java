package me.piggypiglet.gary.commands.punishment.warn;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.mysql.WarningsUtils;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class DelWarn extends Command {
    public DelWarn() {
        super("unwarn");
        options.setRole(Roles.TRUSTED).save();
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            long id = 0L;
            User user = null;

            try {
                id = Long.parseLong(args[0]);
                user = e.getGuild().getMemberById(id).getUser();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (id != 0L) {
                if (user != null) {
                    String nameDiscrim = user.getName() + "#" + user.getDiscriminator();

                    if (WarningsUtils.remove(user.getIdLong(), 1)) {
                        e.getChannel().sendMessage(Lang.getString("commands.punishment.warn.del.success", nameDiscrim)).queue();
                    } else {
                        e.getChannel().sendMessage(Lang.getString("commands.punishment.warn.del.failure", nameDiscrim)).queue();
                    }
                } else {
                    e.getChannel().sendMessage(Lang.getString("commands.punishment.warn.invalid-user")).queue();
                }
            } else {
                e.getChannel().sendMessage(Lang.getString("commands.punishment.warn.invalid-id")).queue();
            }
        } else {
            e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "warn <user id>")).queue();
        }
    }
}
