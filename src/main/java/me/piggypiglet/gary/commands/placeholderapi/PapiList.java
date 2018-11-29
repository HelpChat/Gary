package me.piggypiglet.gary.commands.placeholderapi;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
import me.piggypiglet.gary.core.objects.paginations.PaginationBuilder;
import me.piggypiglet.gary.core.objects.paginations.PaginationPage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PapiList extends Command {
    @Inject private Gson gson;
    @Inject private PaginationHandler paginationHandler;

    public PapiList() {
        super("papi list");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://api.extendedclip.com/");
        List<String> keys = new ArrayList<>();

        try {
            HttpResponse response = client.execute(get);
            keys.addAll(gson.fromJson(EntityUtils.toString(response.getEntity()), LinkedTreeMap.class).keySet());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        keys = Arrays.asList(String.join("\n", keys).replaceAll("((.*\\s*\\n\\s*){25})", "$1-SEPARATOR-\n").split("-SEPARATOR-"));
        String[] title = new String[]{"Expansions in the eCloud", "https://api.extendedclip.com/all"};

        if (keys.size() == 1) {
            e.getChannel().sendMessage(new EmbedBuilder()
            .setTitle(title[0], title[1])
            .setDescription(keys.get(0))
            .setTimestamp(ZonedDateTime.now())
            .build()).queue();
        } else {
            List<String> unicodes = new ArrayList<>();

            Stream.of(
                    "1\u20E3", "2\u20E3", "3\u20E3", "4\u20E3", "5\u20E3", "6\u20E3", "7\u20E3", "8\u20E3", "9\u20E3"
            ).forEach(unicodes::add);

            AtomicInteger i = new AtomicInteger(0);
            PaginationBuilder paginationBuilder = new PaginationBuilder();

            keys.forEach(someKeys -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(title[0], title[1])
                        .setTimestamp(ZonedDateTime.now());

                PaginationPage page = new PaginationPage(embed.setDescription(someKeys).build(), unicodes.get(i.getAndIncrement()));
                paginationBuilder.addPages(page);
            });

            paginationBuilder.build(e.getChannel(), paginationHandler);
        }
    }
}
