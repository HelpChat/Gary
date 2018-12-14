package me.piggypiglet.gary.commands.faq;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.mysql.FaqUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RemoveFaq extends Command {
    public RemoveFaq() {
        super("faq remove", "faq delete");
        options.setRole(Roles.HELPFUL).setDescription("Remove a FAQ from the database.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            if (FaqUtils.remove(args[0])) {
                e.getChannel().sendMessage(Lang.getString("commands.faq.remove.success", args[0])).queue();
            } else {
                e.getChannel().sendMessage(Lang.getString("commands.faq.remove.failure", args[0])).queue();
            }
        } else {
            e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "faq remove <key>")).queue();
        }
    }
}
