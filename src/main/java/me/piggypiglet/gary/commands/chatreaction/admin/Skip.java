package me.piggypiglet.gary.commands.chatreaction.admin;

import com.google.inject.Inject;
import me.piggypiglet.gary.ChatReaction;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;


// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Skip extends Command {
    @Inject private ChatReaction chatReaction;
    @Inject private RoleUtils roleUtils;

    public Skip() {
        super("?cr skip", "Admin command.", false);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (e.getChannel().getIdLong() == Constants.CR) {
            if (roleUtils.isStaff(e.getMember())) {
                e.getChannel().sendMessage(chatReaction.getCurrentWord()).queue();
                chatReaction.generateNewWord();
            }
        }
    }
}
