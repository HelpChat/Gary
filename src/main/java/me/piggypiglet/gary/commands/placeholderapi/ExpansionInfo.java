package me.piggypiglet.gary.commands.placeholderapi;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.pagination.PaginationBuilder;
import me.piggypiglet.gary.core.objects.pagination.PaginationPage;
import me.piggypiglet.gary.core.utils.web.papi.PlaceholderUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ExpansionInfo extends Command {
    @Inject private PlaceholderUtils papiutils;
    @Inject private PaginationBuilder paginationBuilder;

    public ExpansionInfo() {
        super("?papi placeholders /?placeholderapi placeholders /?papi p /?placeholderapi p ", "Get the placeholders in a papi expansion.", true);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (args.length != 3) {
            e.getChannel().sendMessage("Incorrect usage: ?papi placeholders <expansion-name>").queue();
            return;
        }

        papiutils.getJson(args[2]);
        String papi = papiutils.loadJson();
        User author = e.getAuthor();

        switch (papi) {
            case "fail - getJson":
                e.getChannel().sendMessage(author.getAsMention() + "\nI cannot contact the ecloud, please retry at a later date.").queue();
                break;
            case "fail - Error":
                e.getChannel().sendMessage(author.getAsMention() + "\nUnknown expansion.").queue();
                break;
            case "success":
                String title = "Placeholders for " + args[2] + " - version: " + papiutils.getVersion();
                MessageEmbed.Field command = new MessageEmbed.Field("Command:", "```/papi ecloud download " + args[2] + "\n/papi reload```", false);
                String[] footer = { "Author - " + papiutils.getAuthor(), "https://avatars1.githubusercontent.com/u/37001286?s=200&v=4" };

                List<String> placeholders = Arrays.asList(papiutils.getPlaceholders()
                        .replaceAll("((.*\\s*\\n\\s*){20})", "$1-SEPARATOR-\n")
                        .split("-SEPARATOR-"));

                if (placeholders.size() == 1) {
                    MessageEmbed message = new EmbedBuilder()
                            .setTitle(title)
                            .addField(new MessageEmbed.Field("Placeholders: ", papiutils.getPlaceholders() + "\n\u200C", false))
                            .addField(command)
                            .setFooter(footer[0], footer[1])
                            .build();

                    e.getChannel().sendMessage(message).queue();
                } else {
                    List<String> unicodes = new ArrayList<>();

                    Stream.of(
                            "1\u20E3", "2\u20E3", "3\u20E3", "4\u20E3", "5\u20E3", "6\u20E3", "7\u20E3", "8\u20E3", "9\u20E3"
                    ).forEach(unicodes::add);

                    AtomicInteger i = new AtomicInteger(0);

                    placeholders.forEach(somePlaceholders -> {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(title)
                                .setFooter(footer[0], footer[1]);

                        PaginationPage page = new PaginationPage().set(embed.addField(new MessageEmbed.Field("Placeholders:", somePlaceholders + "\n\u200C", false)).addField(command).build(), unicodes.get(i.getAndIncrement()));
                        paginationBuilder.addPages(page);
                    });

                    paginationBuilder.build(e.getTextChannel());
                }

                break;
        }
    }
}
