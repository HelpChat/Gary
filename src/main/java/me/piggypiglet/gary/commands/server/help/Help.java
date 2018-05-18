package me.piggypiglet.gary.commands.server.help;

import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public final class Help extends Command {

    public Help() {
        super("?help", "This page.", true);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        String help = "Welcome to Gary's help menu. Useful things are found here.\n\n" +
                "**Commands** - `?commands`\n" +
                "**Bug reports/suggestions** - https://github.com/help-chat/Gary/issues\n" +
                "**Gary's website** - click `Help` at the top of this message.\n" +
                "**AI** - Interact with Gary's AI in <#" + Constants.BOT + "> via !, eg: `!hi`";
        MessageEmbed message = new EmbedBuilder()
                .setTitle("Help", "https://garys.life")
                .setThumbnail(Constants.AVATAR)
                .setFooter("Gary v" + Constants.VERSION, Constants.AVATAR)
                .appendDescription(help)
                .build();

        e.getChannel().sendMessage(message).complete().delete().queueAfter(2, TimeUnit.MINUTES);
    }
}
