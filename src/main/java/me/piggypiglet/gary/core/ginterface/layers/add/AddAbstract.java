package me.piggypiglet.gary.core.ginterface.layers.add;

import lombok.Getter;
import me.piggypiglet.gary.core.ginterface.layers.InterfaceAbstract;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ginterface.types.AddType;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class AddAbstract extends InterfaceAbstract {
    @Getter private final AddType type;

    protected AddAbstract() {
        this(null);
    }

    protected AddAbstract(AddType type) {
        super(TopEnum.ADD);
        this.type = type;
    }

    protected abstract void execute(GuildMessageReceivedEvent e);

    void run(GuildMessageReceivedEvent e) {
        execute(e);
    }
}
