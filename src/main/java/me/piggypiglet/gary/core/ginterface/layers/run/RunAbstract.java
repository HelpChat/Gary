package me.piggypiglet.gary.core.ginterface.layers.run;

import lombok.Getter;
import me.piggypiglet.gary.core.ginterface.layers.InterfaceAbstract;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ginterface.types.RunType;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class RunAbstract extends InterfaceAbstract {
    @Getter private final RunType type;

    protected RunAbstract() {
        this(null);
    }

    protected RunAbstract(RunType type) {
        super(TopEnum.RUN_EXECUTE);
        this.type = type;
    }

    protected abstract void execute(GuildMessageReceivedEvent e);

    void run(GuildMessageReceivedEvent e) {
        execute(e);
    }
}
