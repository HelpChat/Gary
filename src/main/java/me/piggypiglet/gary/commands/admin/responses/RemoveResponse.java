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
public final class RemoveResponse extends Command {
    @Inject private RoleUtils roleUtils;
    @Inject private Faq faq;

    public RemoveResponse() {
        super("?delfaq", "Delete a faq response", false);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (roleUtils.isStaff(e.getMember())) {
            if (args.length == 1) {
                faq.removeFaq(args[0]);
            }
        }
    }
}
