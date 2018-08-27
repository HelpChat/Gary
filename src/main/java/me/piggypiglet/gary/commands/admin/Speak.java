package me.piggypiglet.gary.commands.admin;

import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Speak extends Command {
    public Speak() {
        super("1 /2 /3 /4 /5 /6 /7 /8 /9 /10", "", false);
        this.delete = false;
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        MessageChannel channel = e.getChannel();
        if (channel.getIdLong() != Constants.SPEAK) {
            return;
        }

        Message message = e.getMessage();
        long channelId;
        int toReplace;

        switch (message.getContentRaw().split(" ")[0]) {
            case "1":
                channelId = Constants.PLUGIN;
                toReplace = 1;
                break;

            case "2":
                channelId = Constants.DEV;
                toReplace = 2;
                break;

            case "3":
                channelId = Constants.RMS;
                toReplace = 3;
                break;

            case "4":
                channelId = Constants.IDEAS;
                toReplace = 4;
                break;

            case "5":
                channelId = Constants.REQUEST_FREE;
                toReplace = 5;
                break;

            case "6":
                channelId = Constants.REQUEST_PAID;
                toReplace = 6;
                break;

            case "7":
                channelId = Constants.OFFER;
                toReplace = 7;
                break;

            case "8":
                channelId = Constants.MC;
                toReplace = 8;
                break;

            case "9":
                channelId = Constants.OTHER;
                toReplace = 9;
                break;

            case "10":
                channelId = Constants.GIVEAWAY_CHANNEL;
                toReplace = 10;
                break;

            default:
                return;
        }

        Guild guild = e.getGuild();
        guild.getTextChannelById(channelId).sendMessage(message.getContentRaw().replace(toReplace + " ", "")).queue();
    }

}
