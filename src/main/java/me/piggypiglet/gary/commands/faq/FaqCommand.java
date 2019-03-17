package me.piggypiglet.gary.commands.faq;

import me.piggypiglet.gary.core.framework.commands.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class FaqCommand extends Command {
    public FaqCommand() {
        super("faq");
        options.setDescription("Retired command.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage("This command is retired, please use <@532800069565546496>'s `=faq <faq name>` command.").queue();
    }
}
