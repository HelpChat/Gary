package me.piggypiglet.gary.commands.admin;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.channel.ChannelUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class PurgeChannel extends Command {
    @Inject private ChannelUtils channelUtils;

    public PurgeChannel() {
        super("?purge ");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (e.getAuthor().getIdLong() == 181675431362035712L) {
            if (args.length == 1) {
                channelUtils.purgeChannel(e.getJDA(), e.getChannel().getIdLong(), Long.valueOf(args[0]));
            }
        }
    }
}
