package me.piggypiglet.gary.core.handlers.chat;

import com.google.inject.Singleton;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.message.StringUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class CommandHandler {
    private List<Command> commands;

    public CommandHandler() {
        commands = new ArrayList<>();
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void check(GuildMessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw();

        for (Command command : commands) {
            String name = command.getName();
            String[] args = msg.toLowerCase().replace(name.toLowerCase(), "").trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            if (StringUtils.startsWith(msg, name)) {
                command.run(e, args[0].isEmpty() ? new String[]{} : args);

                if (command.getDelete()) {
                    e.getMessage().delete().queue();
                }

                return;
            }
        }

        if (Pattern.compile("^[?][a-zA-Z0-9]").matcher(msg).find())
        e.getChannel().sendMessage("Unknown command, run `?help` for help.").queue();
    }
}
