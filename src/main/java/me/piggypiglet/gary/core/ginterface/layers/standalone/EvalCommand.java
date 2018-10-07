package me.piggypiglet.gary.core.ginterface.layers.standalone;

import me.piggypiglet.gary.core.ginterface.Top;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class EvalCommand extends Top {
    public EvalCommand() {
        super(TopEnum.EVAL);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e) {

    }
}
