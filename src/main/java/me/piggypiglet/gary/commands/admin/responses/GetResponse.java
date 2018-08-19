package me.piggypiglet.gary.commands.admin.responses;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.storage.mysql.tables.Faq;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GetResponse extends Command {
    @Inject private Faq faq;

    public GetResponse() {
        super("?faq", "Get a faq response", true);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length == 1) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle(args[0])
                    .setDescription(faq.getFaq(args[0]))
                    .setFooter("Answer by " + faq.getUser(args[0]), null)
                    .setTimestamp(ZonedDateTime.now())
                    .build();

            e.getChannel().sendMessage(embed).queue();
        }
    }
}
