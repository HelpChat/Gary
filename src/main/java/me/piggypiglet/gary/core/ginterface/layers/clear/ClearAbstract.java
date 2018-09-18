package me.piggypiglet.gary.core.ginterface.layers.clear;

import lombok.Getter;
import me.piggypiglet.gary.core.ginterface.layers.InterfaceAbstract;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ginterface.types.ClearType;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class ClearAbstract extends InterfaceAbstract {
    @Getter private final ClearType type;

    protected ClearAbstract() {
        this(null);
    }

    protected ClearAbstract(ClearType type) {
        super(TopEnum.CLEAR_REMOVE);
        this.type = type;
    }

    protected abstract void execute(GuildMessageReceivedEvent e);

    void run(GuildMessageReceivedEvent e) {
        execute(e);
    }
}
