package me.piggypiglet.gary.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.util.MessageUtil;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Speak extends Command {

    private static final long HOME = 373010561794834432L;
    private static final long HELP = 164280494874165248L, DEV = 165129131770511360L, RATE = 339676414137860097L;
    private static final long IDEAS = 339773683469910016L, MC = 382856648064237568L, OTHER = 164523548390457355L;
    @Inject private MessageUtil mutil;

    public Speak() {
        super("1 /2 /3 /4 /5 /6 ");
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        MessageChannel channel = e.getChannel();
        if (channel.getIdLong() != HOME) {
            return;
        }

        Message message = e.getMessage();
        long channelId;
        int toReplace;

        switch (message.getContentRaw().charAt(0)) {
            case '1':
                channelId = HELP;
                toReplace = 1;
                break;

            case '2':
                channelId = DEV;
                toReplace = 2;
                break;

            case '3':
                channelId = RATE;
                toReplace = 3;
                break;

            case '4':
                channelId = IDEAS;
                toReplace = 4;
                break;

            case '5':
                channelId = MC;
                toReplace = 5;
                break;

            case '6':
                channelId = OTHER;
                toReplace = 6;
                break;

            default:
                return;
        }

        Guild guild = e.getGuild();
        guild.getTextChannelById(channelId).sendMessage(mutil.arrayToString(args).replace(String.valueOf(toReplace), "")).queue();
    }

}
