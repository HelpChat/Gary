package me.piggypiglet.gary.commands.standalone;

import me.piggypiglet.gary.core.framework.commands.Command;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by GlareMasters
 * Date: 10/29/2018
 * Time: 4:00 PM
 */
public final class SlowModeCommand extends Command {


    public SlowModeCommand() {
        super("slowmode");
        argPattern = "slowmode-";
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        TextChannel channel = e.getChannel();
        channel.getManager().setSlowmode(Integer.valueOf(args[0]));
        channel.sendMessage("Slow mode has been set to " + args[0] + " seconds!").queue();
    }
}