package me.piggypiglet.gary.commands.faq;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.objects.faq.Faq;
import me.piggypiglet.gary.core.objects.paginations.PaginationBuilder;
import me.piggypiglet.gary.core.objects.paginations.PaginationPage;
import me.piggypiglet.gary.core.storage.file.GFile;
import me.piggypiglet.gary.core.utils.mysql.FaqUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ListFaq extends Command {
    @Inject private GFile gFile;
    @Inject private PaginationHandler paginationHandler;

    public ListFaq() {
        super("faq list", "faqs");
        options.setRole(Roles.HELPFUL).save();
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        List<String> faqs = Arrays.asList(String.join("\n", FaqUtils.getFaqs().stream().map(Faq::getKey).collect(Collectors.toList()))
                .replaceAll("((.*\\s*\\n\\s*){25})", "$1-SEPARATOR-\n")
                .split("-SEPARATOR-"));
        String title = "Faqs";
        String[] footer = new String[]{"Gary v" + gFile.getFileConfiguration("embed").getString("version"), e.getJDA().getSelfUser().getEffectiveAvatarUrl()};

        if (faqs.size() == 1) {
            e.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle(title)
                    .setDescription(faqs.get(0))
                    .setFooter(footer[0], footer[1])
                    .setTimestamp(ZonedDateTime.now())
                    .build()).queue(s -> s.delete().queueAfter(5, TimeUnit.MINUTES));
        } else {
            List<String> unicodes = new ArrayList<>();

            Stream.of(
                    "1\u20E3", "2\u20E3", "3\u20E3", "4\u20E3", "5\u20E3", "6\u20E3", "7\u20E3", "8\u20E3", "9\u20E3"
            ).forEach(unicodes::add);

            AtomicInteger i = new AtomicInteger(0);
            PaginationBuilder paginationBuilder = new PaginationBuilder();

            faqs.forEach(someFaqs -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(title)
                        .setFooter(footer[0], footer[1])
                        .setTimestamp(ZonedDateTime.now());

                paginationBuilder.addPages(new PaginationPage(embed.setDescription(someFaqs).build(), unicodes.get(i.getAndIncrement())));
            });

            paginationBuilder.build(e.getChannel(), paginationHandler);
        }
    }
}
