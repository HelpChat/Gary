package me.piggypiglet.gary.core.ginterface.layers.clear;

import com.google.inject.Singleton;
import me.piggypiglet.gary.core.ginterface.Top;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ginterface.types.ClearType;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class ClearCommands extends Top {
    private List<ClearAbstract> clearTypes;

    public ClearCommands() {
        super(TopEnum.CLEAR_REMOVE);
        clearTypes = new ArrayList<>();
    }

    public List<ClearAbstract> getClearTypes() {
        return clearTypes;
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentStripped();

        for (ClearType value : ClearType.values()) {
            String strValue = value.toString().toLowerCase();

            if (StringUtils.contains(message, strValue)) {
                executeClear(value, e);
            }
        }
    }

    private void executeClear(ClearType type, GuildMessageReceivedEvent e) {
        for (ClearAbstract clearAbstract : clearTypes) {
            if (clearAbstract.getType() == type) {
                clearAbstract.run(e);
            }
        }
    }
}