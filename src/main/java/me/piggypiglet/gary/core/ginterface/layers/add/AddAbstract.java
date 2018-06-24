package me.piggypiglet.gary.core.ginterface.layers.add;

import me.piggypiglet.gary.core.ginterface.layers.InterfaceAbstract;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ginterface.clear.AddType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class AddAbstract extends InterfaceAbstract {
    private final AddType type;

    protected AddAbstract() {
        this(null);
    }

    protected AddAbstract(AddType type) {
        super(TopEnum.ADD);
        this.type = type;
    }

    protected abstract void execute(MessageReceivedEvent e);

    void run(MessageReceivedEvent e) {
        execute(e);
    }

    AddType getType() {
        return type;
    }
}
