package me.piggypiglet.gary.core.ai;

import me.piggypiglet.gary.core.objects.enums.ai.TopEnum;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me

// ------------------------------
public abstract class Top {
    private final TopEnum type;

    protected Top() {
        this(null);
    }

    protected Top(TopEnum type) {
        this.type = type;
    }

    protected abstract void execute(MessageReceivedEvent e);

    public void run(MessageReceivedEvent e) {
        execute(e);
    }

    public TopEnum getType() {
        return type;
    }
}
