package me.piggypiglet.gary.core.handlers.chat;

import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.framework.ginterface.Command;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import static me.piggypiglet.gary.core.objects.enums.EventsEnum.MESSAGE_CREATE;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class InterfaceHandler extends GEvent {
    @Getter private final List<Command> commands = new ArrayList<>();

    public InterfaceHandler() {
        super(MESSAGE_CREATE);
    }

    @Override
    protected void execute(Event event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;

        if (e.getAuthor().getIdLong() == Constants.PIGGYPIGLET) {
            String message = e.getMessage().getContentRaw();

            if (StringUtils.startsWith(message, "gary,/gary/!")) {
                for (Command command : commands) {
                    if (StringUtils.contains(message, command.getCommands())) {
                        if (command.getArgPattern() != null && !StringUtils.endsWith(message, command.getCommands())) {
                            command.run(e, StringUtils.commandSplit(message.replace("```", "\""), command.getArgPattern()));
                        } else {
                            command.run(e, null);
                        }
                    }
                }
            }
        }
    }
}
