package me.piggypiglet.gary.commands.placeholderapi;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.web.papi.PlaceholderUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class ExpansionInfo extends Command {
    @Inject private PlaceholderUtils papiutils;

    public ExpansionInfo() {
        super("?papi placeholders /?placeholderapi placeholders /?papi p /?placeholderapi p ");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (args.length != 3) {
            e.getChannel().sendMessage("Incorrect usage: ?papi placeholders <expansion-name>").queue();
            return;
        }

        papiutils.getJson(args[2]);
        String papi = papiutils.loadJson();

        switch (papi) {
            case "fail - getJson":
                e.getChannel().sendMessage("I cannot contact the ecloud, please retry at a later date.").queue();
                break;
            case "fail - Error":
                e.getChannel().sendMessage("Unknown expansion.").queue();
                break;
            case "success":
                MessageEmbed.Field field = new MessageEmbed.Field("Placeholders", papiutils.getPlaceholders().replace("_", "\\_"), true);
                MessageEmbed msg = new EmbedBuilder()
                        .setTitle("Placeholders for " + args[2] + " - version: " + papiutils.getVersion())
                        .addField(field)
                        .setFooter("Author - " + papiutils.getAuthor() + " - `/papi ecloud download " + args[2] + "`", "https://avatars1.githubusercontent.com/u/37001286?s=200&v=4")
                        .build();
                e.getChannel().sendMessage(msg).queue();
        }
    }
}
