package me.piggypiglet.gary.core.ai.layers.clear;

import me.piggypiglet.gary.core.ai.InterfaceAbstract;
import me.piggypiglet.gary.core.objects.enums.ai.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ai.clear.ClearType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class ClearAbstract extends InterfaceAbstract {
    private final ClearType type;

    protected ClearAbstract() {
        this(null);
    }

    protected ClearAbstract(ClearType type) {
        super(TopEnum.CLEAR);
        this.type = type;
    }

    protected abstract void execute(MessageReceivedEvent e);

    void run(MessageReceivedEvent e) {
        execute(e);
    }

    ClearType getType() {
        return type;
    }
}
