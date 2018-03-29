package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.commands.*;
import me.piggypiglet.gary.commands.placeholderapi.Info;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.channel.MessageUtils;
import me.piggypiglet.gary.core.utils.channel.RMSUtils;
import me.piggypiglet.gary.core.utils.channel.RequestUtils;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class CommandHandler extends ListenerAdapter {

    @Inject private RequestUtils rutil;
    @Inject private RMSUtils rmsutil;
    @Inject private MessageUtils mutil;
    @Inject private AI ai;
    @Inject private Speak speak;
    @Inject private RoleID roleID;
    @Inject private BanCheck banCheck;
    @Inject private Suggestion suggestion;
    @Inject private Info info;
    private Command[] commands;

    public CommandHandler() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        commands = new Command[] { ai, speak, roleID, banCheck, suggestion, info };
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw();

        if (!e.getAuthor().isBot()) {
            for (Command command : commands) {
                String name = command.getName();
                String[] args = msg.toLowerCase().replace(name.toLowerCase(), "").trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                // TODO: Fix args to make it pickup slashes in a command name
                if (mutil.startsWith(msg, name)) {
                    command.run(e, args);
                }
            }
            checkMessage(e);
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent e) {
        if (!e.getAuthor().isBot()) {
            checkMessage(e);
        }
    }

    private void checkMessage(GenericMessageEvent e) {
        if (e.getChannel().getIdLong() == Constants.REQUEST) {
            rutil.checkMessage(e);
        }
        if (e.getChannel().getIdLong() == Constants.RMS) {
            rmsutil.createMessage(e);
        }
    }

}
