package me.piggypiglet.gary.commands;

import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Speak extends Command {
    public Speak() {
        super("1 /2 /3 /4 /5 /6 ");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        MessageChannel channel = e.getChannel();
        if (channel.getIdLong() != Constants.SPEAK) {
            return;
        }

        Message message = e.getMessage();
        long channelId;
        int toReplace;

        switch (message.getContentRaw().charAt(0)) {
            case '1':
                channelId = Constants.PLUGIN;
                toReplace = 1;
                break;

            case '2':
                channelId = Constants.DEV;
                toReplace = 2;
                break;

            case '3':
                channelId = Constants.RMS;
                toReplace = 3;
                break;

            case '4':
                channelId = Constants.IDEAS;
                toReplace = 4;
                break;

            case '5':
                channelId = Constants.REQUEST;
                toReplace = 5;
                break;

            case '6':
                channelId = Constants.OFFER;
                toReplace = 6;
                break;

            case '7':
                channelId = Constants.MC;
                toReplace = 6;
                break;

            case '8':
                channelId = Constants.OTHER;
                toReplace = 6;
                break;

            case '9':
                channelId = Constants.BOT;
                toReplace = 6;
                break;

            default:
                return;
        }

        Guild guild = e.getGuild();
        guild.getTextChannelById(channelId).sendMessage(message.getContentRaw().replace(String.valueOf(toReplace) + " ", "")).queue();
    }

}
