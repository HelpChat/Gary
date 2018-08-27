package me.piggypiglet.gary.commands.admin.channel;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import me.piggypiglet.gary.core.utils.misc.ChannelUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

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
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (roleUtils.isTrustedPlus(e.getMember())) {
            if (args.length == 1) {
                channelUtils.purgeChannel(e.getChannel(), e.getMessageId(), Integer.valueOf(args[0]), true);
            }
        }
    }
}
