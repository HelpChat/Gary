package me.piggypiglet.gary.core.handlers.chat;

import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.discord.RoleUtils;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static me.piggypiglet.gary.core.objects.enums.EventsEnum.MESSAGE_CREATE;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class CommandHandler extends GEvent {
    @Getter
    private final List<Command> commands = new ArrayList<>();

    public CommandHandler() {
        super(MESSAGE_CREATE);
    }

    @Override
    protected void execute(Event event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;

        String message = e.getMessage().getContentRaw().toLowerCase();
        List<String> prefixs = Lang.getStringList("commands.prefix");

        if (StringUtils.startsWith(message, String.join("/", prefixs))) {
            for (String str : prefixs) {
                message = message.replaceFirst(str, "");
            }

            for (Command command : commands) {
                if (StringUtils.startsWith(message, command.getCommands())) {
                    String[] args = StringUtils.commandSplit(message, command.getCommands());

                    switch (command.getAllowedRole()) {
                        case EVERYBODY:
                            command.run(e, args);
                            return;

                        case HELPFUL:
                            if (RoleUtils.isHelpful(e.getMember())) {
                                command.run(e, args);
                                return;
                            }
                            break;

                        case TRUSTED:
                            if (RoleUtils.isTrusted(e.getMember())) {
                                command.run(e, args);
                                return;
                            }
                            break;

                        case ADMIN:
                            if (RoleUtils.isAdmin(e.getMember())) {
                                command.run(e, args);
                                return;
                            }
                            break;
                    }

                    e.getChannel().sendMessage(Lang.getString("commands.no-permission")).queue(s -> s.delete().queueAfter(15, TimeUnit.SECONDS));
                }
            }
        }
    }
}
