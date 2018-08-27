package me.piggypiglet.gary.commands.admin.responses;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.storage.mysql.tables.Faq;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class AddResponse extends Command {
    @Inject private RoleUtils roleUtils;
    @Inject private Faq faq;

    public AddResponse() {
        super("?setfaq", "Set a faq", false);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (roleUtils.isStaff(e.getMember())) {
            if (args.length == 2) {
                faq.addFaq(args[0], args[1].replace("\"", ""), e.getAuthor().getIdLong());
            }
        }
    }
}
