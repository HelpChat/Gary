package me.piggypiglet.gary.commands.admin;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.giveaways.GiveawayBuilder;
import me.piggypiglet.gary.core.storage.mysql.tables.Giveaways;
import me.piggypiglet.gary.core.tasks.GiveawayTask;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Giveaway extends Command {
    @Inject private Giveaways giveaways;
    @Inject private MessageUtils messageUtils;

    public Giveaway() {
        super("?giveaway", "Admin Command", false);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (e.getChannel().getIdLong() == Constants.GIVEAWAY_CHANNEL) {
            Message message = e.getMessage();
            List<String> items = new ArrayList<>();
            Stream.of("[prize]", "[time]", "[emoji]").forEach(items::add);

            if (messageUtils.contains(message.getContentRaw(), items)) {
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

                GiveawayBuilder giveawayBuilder = new GiveawayBuilder()
                        .setJDA(e.getJDA())
                        .setPrize(prize)
                        .setTime(time)
                        .setEmoji(emoji);

                long giveaway = giveawayBuilder.build().getIdLong();
                TextChannel channel = e.getTextChannel();

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
