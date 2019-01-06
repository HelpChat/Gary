package me.piggypiglet.gary.core.handlers.chat;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.http.WebUtils;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.piggypiglet.gary.core.objects.enums.EventsEnum.MESSAGE_CREATE;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class HastebinScanner extends GEvent {
    public HastebinScanner() {
        super(MESSAGE_CREATE);
    }

    @Override
    protected void execute(Event event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;
        String messageContent = e.getMessage().getContentRaw().toLowerCase();

        if (StringUtils.contains(messageContent, "hastebin.com/paste.helpch.at")) {
            List<String> urls = new ArrayList<>();
            Matcher matcher = Pattern.compile("((https?):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", Pattern.CASE_INSENSITIVE).matcher(messageContent);

            while (matcher.find()) {
                urls.add(messageContent.substring(matcher.start(0), matcher.end(0)));
            }

            urls.forEach(i -> {
                if (!StringUtils.contains(i, "hastebin.com/paste.helpch.at")) {
                    urls.remove(i);
                } else {
                    String[] rawSplit = i.split("/");
                    rawSplit[0] = rawSplit[0] + "//" + rawSplit[2];
                    rawSplit[3] = "/raw/" + rawSplit[3];

                    try {
                        String[] paste = WebUtils.getStringEntity(rawSplit[0] + rawSplit[3]).split("\n");
                        boolean success = false;

                        StringBuilder catches = new StringBuilder();
                        for (int j = 0; j < paste.length; ++j) {
                            if (StringUtils.contains(paste[j], "crack/cracked/leaked/leak/bsmc/directleaks/leaks/blackspigot/nulled")) {
                                success = true;
                                catches.append("```tex\n$ [").append(j + 1).append(":] ").append(paste[j]).append("```\n");
                            }
                        }

                        if (success) {

                            MessageEmbed message = new EmbedBuilder()
                                    .setColor(Constants.YELLOW)
                                    .setDescription("⚠ " + e.getAuthor().getAsMention() + " just posted a log with this data:" + catches.toString() + "\n[Link to paste](" + i + ")")
                                    .setTimestamp(ZonedDateTime.now())
                                    .build();

                            e.getGuild().getTextChannelById(Constants.STAFF).sendMessage(message).queue();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }
}
