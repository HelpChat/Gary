package me.piggypiglet.gary.commands.chatreaction.admin;

import com.google.inject.Inject;
import me.piggypiglet.gary.ChatReaction;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class SetWord extends Command {
    @Inject private RoleUtils roleUtils;
    @Inject private ChatReaction chatReaction;

    public SetWord() {
        super("?cr setword", "Staff command.", false);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (roleUtils.isStaff(e.getMember())) {
            if (args.length >= 1) {
                chatReaction.setWord(args[0].contains("\"") ? args[0].split(" ")[0].replace("\"", "") : args[0]);
            }
        }
    }
}
