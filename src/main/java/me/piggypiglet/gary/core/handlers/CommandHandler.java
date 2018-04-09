package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import me.piggypiglet.gary.core.utils.message.RMSUtils;
import me.piggypiglet.gary.core.utils.message.RequestUtils;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class CommandHandler extends ListenerAdapter {

    @Inject private RequestUtils rutil;
    @Inject private RMSUtils rmsutil;
    @Inject private MessageUtils mutil;
    private List<Command> commands;

    public CommandHandler() {
        commands = new ArrayList<>();
    }

    public List<Command> getCommands() {
        return commands;
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
