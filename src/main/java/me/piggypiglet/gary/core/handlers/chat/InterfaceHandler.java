package me.piggypiglet.gary.core.handlers.chat;

import com.google.inject.Singleton;
import me.piggypiglet.gary.core.ginterface.Top;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.utils.message.StringUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class InterfaceHandler {
    private Map<TopEnum, Top> topCommands;

    public InterfaceHandler() {
        topCommands = new HashMap<>();
    }

    public Map<TopEnum, Top> getTopCommands() {
        return topCommands;
    }

    public void run(GuildMessageReceivedEvent e) {
        if (e.getAuthor().getIdLong() == Constants.PIGGYPIGLET) {
            String message = e.getMessage().getContentStripped();

            if (StringUtils.startsWith(message, "gary, /gary ")) {
                for (TopEnum type : TopEnum.values()) {
                    if (StringUtils.contains(message, type.toString())) {
                        topCommands.get(type).run(e);
                    }
                }
            }
        }
    }
}
