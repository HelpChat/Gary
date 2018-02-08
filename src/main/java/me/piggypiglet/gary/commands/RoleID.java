package me.piggypiglet.gary.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.util.RoleUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class RoleID extends Command {
    @Inject private RoleUtil rutil;

    public RoleID() {
        cmd = "?roleid";
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    public void execute(MessageReceivedEvent e, String[] args) {
        if (args.length == 1) {
            if (rutil.getRole(e.getGuild(), args[0]) == null) {
                e.getChannel().sendMessage("Role doesn't exist or isn't found!").queue();
            } else {
                e.getChannel().sendMessage(rutil.getRole(e.getGuild(), args[0]).getId()).queue();
            }
        }
    }
}
