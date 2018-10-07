package me.piggypiglet.gary.core.ginterface;

import lombok.Getter;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class Top {
    @Getter private final TopEnum type;

    protected Top() {
        this(null);
    }

    protected Top(TopEnum type) {
        this.type = type;
    }

    protected abstract void execute(GuildMessageReceivedEvent e);


    public void run(GuildMessageReceivedEvent e) {
        execute(e);
    }
}
