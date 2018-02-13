package me.piggypiglet.gary.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Suggestion extends Command {
    @Inject private MessageUtil mutil;

    public Suggestion() {
        super("?suggestion");

        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        //TODO: Temp solution, change to webhook soonTM or github magic (automatically edit issue).
        if (args[0].length() == 0) {
            e.getChannel().sendMessage("Invalid suggestion").queue();
            return;
        }
        e.getGuild().getTextChannelById(Constants.PIG).sendMessage(mutil.arrayToString(args)).queue();
    }
}
