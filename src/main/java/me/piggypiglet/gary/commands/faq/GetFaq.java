package me.piggypiglet.gary.commands.faq;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.mysql.FaqUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GetFaq extends Command {
    public GetFaq() {
        super("faq get");
        options.setRole(Roles.HELPFUL).setDescription("Print a FAQ out in the channel this command is ran in.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            String faq = FaqUtils.get(args[0]);

            if (!faq.equalsIgnoreCase("null")) {
                if (args.length >= 2 && args[1].equalsIgnoreCase("raw")) {
                    e.getChannel().sendMessage("```" + faq + "```").queue();
                } else {
                    e.getChannel().sendMessage(faq).queue();
                }
            } else {
                e.getChannel().sendMessage(Lang.getString("commands.faq.get.failure", args[0])).queue();
            }
        } else {
            e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "faq get <key>")).queue();
        }
    }
}
