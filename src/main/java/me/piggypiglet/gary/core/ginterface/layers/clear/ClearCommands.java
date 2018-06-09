package me.piggypiglet.gary.core.ginterface.layers.clear;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.ginterface.Top;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ginterface.clear.ClearType;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class ClearCommands extends Top {
    @Inject private MessageUtils messageUtils;

    private List<ClearAbstract> clearTypes;

    public ClearCommands() {
        super(TopEnum.CLEAR);
        clearTypes = new ArrayList<>();
    }

    public List<ClearAbstract> getClearTypes() {
        return clearTypes;
    }

    @Override
    protected void execute(MessageReceivedEvent e) {
        String message = e.getMessage().getContentStripped();

        for (ClearType value : ClearType.values()) {
            String strValue = value.toString().toLowerCase();

            if (messageUtils.contains(message, strValue)) {
                executeClear(value, e);
            }
        }
    }

    private void executeClear(ClearType type, MessageReceivedEvent e) {
        for (ClearAbstract clearAbstract : clearTypes) {
            if (clearAbstract.getType() == type) {
                clearAbstract.run(e);
            }
        }
    }
}
