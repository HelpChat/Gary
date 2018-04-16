package me.piggypiglet.gary.commands.chatreaction;

import com.google.inject.Inject;
import me.piggypiglet.gary.ChatReaction;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class NewWord extends Command {
    @Inject private ChatReaction cr;
    @Inject private RoleUtils roleUtils;

    public NewWord() {
        super("?cr newword", "", false);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (e.getChannel().getIdLong() == Constants.CR) {
            if (roleUtils.isStaff(e.getMember())) {
                cr.generateNewWord();
            }
        }
    }
}
