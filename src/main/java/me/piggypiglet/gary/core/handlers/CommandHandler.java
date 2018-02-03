package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.commands.AI;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.util.RequestUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public class CommandHandler extends ListenerAdapter {
    @Inject
    private RequestUtil rutil;
    private Command[] commands;

    public CommandHandler() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
        commands = new Command[] {
                new AI()
        };
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            for (Command command : commands) {
                String[] args = e.getMessage().getContentRaw().replace(command.cmd, "").trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if (command.cmd.contains("/")) {
                    String[] cmd = command.cmd.split("/");
                    for (String cmds : cmd) {
                        if (e.getMessage().getContentRaw().toLowerCase().startsWith(cmds)) {
                            command.run(e, args);
                        }
                    }
                } else if (e.getMessage().getContentRaw().toLowerCase().startsWith(command.cmd)) {
                    command.run(e, args);
                }
            }
            if (e.getChannel().getId().equalsIgnoreCase("297996869173379072")) {
                rutil.checkMessage(e);
            }
        }
    }
}
