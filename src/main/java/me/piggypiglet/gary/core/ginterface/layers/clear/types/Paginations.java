package me.piggypiglet.gary.core.ginterface.layers.clear.types;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.ginterface.layers.clear.ClearAbstract;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
import me.piggypiglet.gary.core.objects.enums.ginterface.clear.ClearType;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me

// ------------------------------
public final class Paginations extends ClearAbstract {
    @Inject private PaginationHandler paginationHandler;

    public Paginations() {
        super(ClearType.PAGINATIONS);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e) {
        paginationHandler.clearPaginations();
        e.getChannel().sendMessage("All paginations successfully cleared from memory.").queue();
    }
}
