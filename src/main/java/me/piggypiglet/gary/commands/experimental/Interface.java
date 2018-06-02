package me.piggypiglet.gary.commands.experimental;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.handlers.PaginationHandler;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// https://www.andrewa.pw
// ------------------------------
public final class Interface extends Command {
    @Inject private MessageUtils messageUtils;
    @Inject private PaginationHandler paginationHandler;

    public Interface() {
        super("gary, ", "Admin command.", false);
        this.delete = false;
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        User author = e.getAuthor();

        if (author.getIdLong() == Constants.PIGGYPIGLET) {
            String message = e.getMessage().getContentStripped();

            if (messageUtils.contains(message, "clear/paginations")) {
                paginationHandler.clearPaginations();
                e.getChannel().sendMessage("Successfully cleared all paginations from memory.").queue();
            }
        }
    }
}
