package me.piggypiglet.gary.commands.admin;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.giveaways.GiveawayBuilder;
import me.piggypiglet.gary.core.storage.mysql.tables.Giveaways;
import me.piggypiglet.gary.core.tasks.GiveawayTask;
import me.piggypiglet.gary.core.utils.message.StringUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Giveaway extends Command {
    @Inject private Giveaways giveaways;

    public Giveaway() {
        super("?giveaway", "Admin Command", false);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (e.getChannel().getIdLong() == Constants.GIVEAWAY_CHANNEL) {
            Message message = e.getMessage();
            List<String> items = new ArrayList<>();
            Stream.of("[prize]", "[time]", "[emoji]").forEach(items::add);

            if (StringUtils.contains(message.getContentRaw(), items)) {
                User user = e.getAuthor();

                String prize = "";
                String time = "";
                String emoji = "";

                Pattern p = Pattern.compile("([\\[][\\w]+[]])(.+)");
                Matcher m = p.matcher(message.getContentRaw());

                while (m.find()) {
                    String bracket = m.group(1);
                    String item = m.group(2);

                    switch (bracket.toLowerCase()) {
                        case "[prize]":
                            prize = item.trim();
                            break;
                        case "[time]":
                            time = item.trim();
                            break;
                        case "[emoji]":
                            emoji = item.trim();
                            break;
                    }
                }

                TextChannel channel = e.getChannel();

                List<String> mentions = new ArrayList<>();
                Guild guild = e.getGuild();
                guild.getMembersWithRoles(guild.getRoleById(Constants.GIVEAWAY_ROLE)).forEach(member -> mentions.add(member.getAsMention()));

                int i = 0;
                StringBuilder builder = new StringBuilder();

                for (String mention : mentions) {
                    if (i == 15 || mention.equals(mentions.get(mentions.size() - 1))) {
                        channel.sendMessage(builder.toString()).complete().delete().queueAfter(3, TimeUnit.SECONDS);

                        builder = new StringBuilder();
                        i = 0;
                    }

                    builder.append(mention).append(" ");

                    i++;
                }

                GiveawayBuilder giveawayBuilder = new GiveawayBuilder()
                        .setJDA(e.getJDA())
                        .setPrize(prize)
                        .setTime(time)
                        .setEmoji(emoji);

                long giveaway = giveawayBuilder.build().getIdLong();

                giveaways.newGiveaway(
                        channel,
                        giveaway,
                        giveawayBuilder.getTime().getMilliseconds(),
                        user.getName() + "#" + user.getDiscriminator(),
                        prize,
                        new GiveawayTask(giveaways, giveaway, channel, prize)
                );
            }
        }
    }
}
