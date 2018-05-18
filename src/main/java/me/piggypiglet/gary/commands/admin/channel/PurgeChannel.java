package me.piggypiglet.gary.commands.admin.channel;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import me.piggypiglet.gary.core.utils.misc.ChannelUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PurgeChannel extends Command {
    @Inject private ChannelUtils channelUtils;
    @Inject private RoleUtils roleUtils;

    public PurgeChannel() {
        super("?purge ", "", false);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (roleUtils.isTrustedPlus(e.getMember())) {
            if (args.length == 1) {
                if (e.getChannel().getMessageById(args[0]) != null) {
                    channelUtils.purgeChannel(e.getTextChannel(), args[0], 100, false);
                } else {
                    channelUtils.purgeChannel(e.getTextChannel(), e.getMessageId(), Integer.valueOf(args[0]), true);
                }
            }
        }
    }
}
