package me.piggypiglet.gary.commands.server.help;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.handlers.CommandHandler;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class Commands extends Command {
    @Inject private CommandHandler commandHandler;
    @Inject private MessageUtils messageUtils;

    public Commands() {
        super("?commands", "Command list.", true);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        StringBuilder description = new StringBuilder();

        for (Command command : commandHandler.getCommands()) {
            if (command.getHelp()) {
                description.append("**").append(messageUtils.getFirst(command.getName())).append("** - ").append(command.getDescription()).append("\n");
            }
        }

        MessageEmbed message = new EmbedBuilder()
                .setTitle("Commands")
                .setFooter("Gary v" + Constants.VERSION, Constants.AVATAR)
                .setThumbnail(Constants.AVATAR)
                .appendDescription(description)
                .build();

        e.getChannel().sendMessage(message).complete().delete().queueAfter(3, TimeUnit.MINUTES);
    }
}
