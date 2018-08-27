package me.piggypiglet.gary.commands.server.help;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.handlers.chat.CommandHandler;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import me.piggypiglet.gary.core.utils.message.StringUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public final class Commands extends Command {
    @Inject private CommandHandler commandHandler;
    @Inject private RoleUtils roleUtils;

    public Commands() {
        super("?commands", "Command list.", true);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        StringBuilder description = new StringBuilder();

        commandHandler.getCommands().forEach(cmd -> {
            if (roleUtils.isTrustedPlus(e.getMember()) || cmd.getHelp()) {
                description.append("**").append(StringUtils.getFirst(cmd.getName())).append("** - ").append(cmd.getDescription()).append("\n");
            }
        });

        MessageEmbed message = new EmbedBuilder()
                .setTitle("Commands")
                .setFooter("Gary v" + Constants.VERSION, Constants.AVATAR)
                .setThumbnail(Constants.AVATAR)
                .appendDescription(description)
                .build();

        e.getChannel().sendMessage(message).complete().delete().queueAfter(3, TimeUnit.MINUTES);
    }
}
