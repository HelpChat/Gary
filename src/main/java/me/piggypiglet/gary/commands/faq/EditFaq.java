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
public final class EditFaq extends Command {
    public EditFaq() {
        super("faq edit", "faq change");
        options.setRole(Roles.HELPFUL).save();
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 2) {
            if (FaqUtils.edit(args[0], args[1].replace("\"", ""))) {
                e.getChannel().sendMessage(Lang.getString("commands.faq.edit.success", args[0], args[1])).queue();
            } else {
                e.getChannel().sendMessage(Lang.getString("commands.faq.edit.failure", args[0], args[1])).queue();
            }
        } else {
            e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "faq edit <key> <value>")).queue();
        }
    }
}
