package me.piggypiglet.gary.commands.placeholderapi;

import com.google.gson.Gson;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
import me.piggypiglet.gary.core.objects.paginations.PaginationBuilder;
import me.piggypiglet.gary.core.objects.paginations.PaginationPage;
import me.piggypiglet.gary.core.objects.placeholderapi.PapiExpansion;
import me.piggypiglet.gary.core.storage.file.Lang;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PapiGet extends Command {
    @Inject private Gson gson;
    @Inject private PaginationHandler paginationHandler;

    public PapiGet() {
        super("papi get");
        options.setDescription("Get placeholders for an expansion in the eCloud.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            PapiExpansion exp = new PapiExpansion(gson).load(args[0]);

            if (exp.isSuccess()) {
                String title = "Placeholders for " + args[0];
                String[] footer = new String[]{"Version " + exp.getVersion() + " by " + exp.getAuthor(), e.getJDA().getSelfUser().getEffectiveAvatarUrl()};
                MessageEmbed.Field command = new MessageEmbed.Field("Commands:", "```/papi ecloud download " + args[0] + "\n/papi reload```", false);
                List<String> placeholders = exp.getPlaceholders();

                if (placeholders.size() == 1) {
                    e.getChannel().sendMessage(new EmbedBuilder()
                            .setTitle(title)
                            .addField("Placeholders:", exp.getPlaceholders().get(0) + "\n\u200C", false)
                            .addField(command)
                            .setFooter(footer[0], footer[1])
                            .setTimestamp(ZonedDateTime.now())
                            .build()).queue();
                } else {
                    List<String> unicodes = new ArrayList<>();

                    Stream.of(
                            "1\u20E3", "2\u20E3", "3\u20E3", "4\u20E3", "5\u20E3", "6\u20E3", "7\u20E3", "8\u20E3", "9\u20E3"
                    ).forEach(unicodes::add);

                    AtomicInteger i = new AtomicInteger(0);
                    PaginationBuilder paginationBuilder = new PaginationBuilder();

                    placeholders.forEach(somePlaceholders -> {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(title)
                                .setFooter(footer[0], footer[1])
                                .setTimestamp(ZonedDateTime.now());

                        PaginationPage page = new PaginationPage(embed.addField("Placeholders:", somePlaceholders + "\n\u200C", false).addField(command).build(), unicodes.get(i.getAndIncrement()));
                        paginationBuilder.addPages(page);
                    });

                    paginationBuilder.build(e.getChannel(), paginationHandler);
                }
            } else {
                e.getChannel().sendMessage(Lang.getString("commands.papi.get.failure")).queue();
            }
        } else {
            e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "papi get <expansion name>")).queue();
        }
    }
}
