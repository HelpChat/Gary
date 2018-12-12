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
public final class AddFaq extends Command {
    public AddFaq() {
        super("faq add", "faq make", "faq create");
        options.setRole(Roles.HELPFUL).save();
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 2) {
            if (FaqUtils.add(args[0], args[1].replace("\"", ""), e.getAuthor().getIdLong())) {
                e.getChannel().sendMessage(Lang.getString("commands.faq.add.success", args[0])).queue();
            } else {
                e.getChannel().sendMessage(Lang.getString("commands.faq.add.failure", args[0])).queue();
            }
        } else {
            e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "faq add <key> <value>")).queue();
        }
    }
}
