package me.piggypiglet.gary.core.handlers.chat;

import com.google.inject.Singleton;
import me.piggypiglet.gary.core.ginterface.Top;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

import static me.piggypiglet.gary.core.objects.enums.EventsEnum.MESSAGE_CREATE;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class InterfaceHandler extends GEvent {
    private Map<TopEnum, Top> topCommands;

    public InterfaceHandler() {
        super(MESSAGE_CREATE);

        topCommands = new HashMap<>();
    }

    public Map<TopEnum, Top> getTopCommands() {
        return topCommands;
    }

    @Override
    protected void execute(Event event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;

        if (e.getAuthor().getIdLong() == 181675431362035712L) {
            String message = e.getMessage().getContentStripped();

            if (StringUtils.startsWith(message, "gary, /gary /!")) {
                for (TopEnum type : TopEnum.values()) {
                    if (StringUtils.contains(message, type.toString().replace("_", "/"))) {
                        topCommands.get(type).run(e);
                    }
                }
            }
        }
    }
}
