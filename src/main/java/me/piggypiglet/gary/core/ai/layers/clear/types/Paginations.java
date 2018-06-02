package me.piggypiglet.gary.core.ai.layers.clear.types;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.ai.layers.clear.ClearAbstract;
import me.piggypiglet.gary.core.handlers.PaginationHandler;
import me.piggypiglet.gary.core.objects.enums.ai.clear.ClearType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
    protected void execute(MessageReceivedEvent e) {
        paginationHandler.clearPaginations();
        e.getChannel().sendMessage("All paginations successfully cleared from memory.").queue();
    }
}
