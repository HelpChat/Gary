package me.piggypiglet.gary.commands.standalone;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.objects.tasks.Task;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import sh.okx.timeapi.TimeAPI;

import java.util.concurrent.TimeUnit;

/**
 * Created by GlareMasters
 * Date: 10/29/2018
 * Time: 4:00 PM
 */
public final class SlowModeCommand extends Command {
    public SlowModeCommand() {
        super("slowmode");
        options.setRole(Roles.TRUSTED);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        TextChannel channel = e.getChannel();
        TimeAPI slowmodeTime = new TimeAPI(args[0]);
        TimeAPI lastTime = new TimeAPI(args[1]);
        
        e.getMessage().delete().queue();
        channel.getManager().setSlowmode((int) slowmodeTime.getSeconds()).queue();

        Task.scheduleAsync(r -> channel.getManager().setSlowmode(0).queue(), lastTime.getMilliseconds(), TimeUnit.MILLISECONDS);

        MessageEmbed message = new EmbedBuilder()
                .setColor(Constants.GREEN)
                .setDescription("\uD83D\uDC22 Slowmode has been activated.")
                .build();

        channel.sendMessage(message).queue();
    }
}
