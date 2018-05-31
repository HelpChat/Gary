package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class CommandHandler extends ListenerAdapter {
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

                if (mutil.startsWith(msg, name)) {
                    command.run(e, args);

                    if (command.getDelete()) {
                        e.getMessage().delete().queue();
                    }
                }
            }
        }
    }

}
