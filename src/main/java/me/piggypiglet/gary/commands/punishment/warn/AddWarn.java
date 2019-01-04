package me.piggypiglet.gary.commands.punishment.warn;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.mysql.WarningsUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class AddWarn extends Command {
    public AddWarn() {
        super("warn");
        options.setRole(Roles.TRUSTED).setDescription("Add a warning to a user.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            long id;
            User user;

            try {
                id = Long.parseLong(args[0]);
                user = e.getGuild().getMemberById(id).getUser();
            } catch (Exception ex) {
                e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "warn <user id>")).queue();
                return;
            }

            if (id != 0L) {
                if (user != null) {
                    String nameDiscrim = user.getName() + "#" + user.getDiscriminator();

                    if (WarningsUtils.add(user.getIdLong(), 1)) {
                        e.getChannel().sendMessage(Lang.getString("commands.punishment.warn.add.success", nameDiscrim)).queue();
                    } else {
                        e.getChannel().sendMessage(Lang.getString("commands.punishment.warn.add.failure", nameDiscrim)).queue();
                    }
                } else {
                    e.getChannel().sendMessage(Lang.getString("commands.punishment.warn.add.invalid-user")).queue();
                }
            } else {
                // TODO: Change this to incorrect-usage
                e.getChannel().sendMessage(Lang.getString("commands.punishment.warn.add.invalid-id")).queue();
            }
        } else {
            e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "warn <user id>")).queue();
        }
    }
}
