package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Injector;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import me.piggypiglet.gary.commands.AI;
import me.piggypiglet.gary.commands.BanCheck;
import me.piggypiglet.gary.commands.RoleID;
import me.piggypiglet.gary.commands.Speak;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.util.MessageUtil;
import me.piggypiglet.gary.core.util.RequestUtil;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class CommandHandler extends ListenerAdapter {

    @Inject private RequestUtil rutil;
    @Inject private MessageUtil mutil;
    @Inject private AI ai;
    @Inject private Speak speak;
    @Inject private RoleID roleID;
    @Inject private BanCheck banCheck;
    private Command[] commands;

    public CommandHandler() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
        commands = new Command[] { ai, speak, roleID, banCheck };
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw();

        if (!e.getAuthor().isBot()) {
            for (Command command : commands) {
                String name = command.getName();
                String[] args = msg.toLowerCase().replace(name.toLowerCase(), "").trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if (mutil.startsWith(msg, name)) {
                    command.run(e, args);
                }
            }

            if (e.getChannel().getIdLong() == 297996869173379072L) {
                rutil.checkMessage(e);
            }
        }
    }

}