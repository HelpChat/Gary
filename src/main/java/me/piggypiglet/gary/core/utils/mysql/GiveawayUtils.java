package me.piggypiglet.gary.core.utils.mysql;

import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.giveaways.GiveawayBuilder;
import me.piggypiglet.gary.core.objects.tasks.tasks.Giveaway;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import sh.okx.timeapi.TimeAPI;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GiveawayUtils {
    @Inject private static GaryBot garyBot;

    public static boolean create(Giveaway giveaway, long messageId) {
        GiveawayBuilder builder = giveaway.getBuilder();

        return MySQLUtils.create(
                "gary_giveaways",
                new String[] {"message_id", "author_id", "prize", "emote", "time", "time_left"},
                messageId, builder.getAuthor().getIdLong(), builder.getPrize(), giveaway.getBuilder().getEmote(), builder.getTime().getOgTime(), builder.getTime().getSeconds()
        );
    }

    public static boolean remove(Giveaway giveaway) {
        CompletableFuture<Message> message = giveaway.getMessage();

        //noinspection StatementWithEmptyBody
        while (!message.isDone()) {}

        try {
            return MySQLUtils.remove("gary_giveaways", new String[]{"message_id"}, new Object[]{message.get().getIdLong()});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<Giveaway> getGiveaways() {
        Guild guild = garyBot.getJda().getGuildById(Constants.GUILD);

        return MySQLUtils.getRows("gary_giveaways").stream().map(r -> new GiveawayBuilder()
                .setAuthor(guild.getMemberById(r.getLong("author_id")).getUser())
                .setEmote(r.getString("emote"))
                .setPrize(r.getString("prize"))
                .setTime(new TimeAPI(r.getString("time")))
                .setTimeLeft(new TimeAPI(r.getLong("time_left") + "secs"))
                .build(garyBot, guild.getTextChannelById(Constants.GIVEAWAY).retrieveMessageById(r.getLong("message_id")).complete())
        ).collect(Collectors.toList());
    }
}
