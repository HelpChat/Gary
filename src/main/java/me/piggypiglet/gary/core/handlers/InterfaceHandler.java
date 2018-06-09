package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.ginterface.Top;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class InterfaceHandler extends ListenerAdapter {
    @Inject private MessageUtils messageUtils;

    private Map<TopEnum, Top> topCommands;

    public InterfaceHandler() {
        topCommands = new HashMap<>();
    }

    public Map<TopEnum, Top> getTopCommands() {
        return topCommands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().getIdLong() == Constants.PIGGYPIGLET) {
            String message = e.getMessage().getContentStripped();

            if (messageUtils.startsWith(message, "gary, /gary ")) {
                for (TopEnum type : TopEnum.values()) {
                    if (messageUtils.contains(message, type.toString())) {
                        topCommands.get(type).run(e);
                    }
                }
            }
        }
    }
}
