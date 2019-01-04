package me.piggypiglet.gary.core.objects.tasks.tasks;

import lombok.Getter;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.handlers.misc.GiveawayHandler;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.giveaways.GiveawayBuilder;
import me.piggypiglet.gary.core.objects.tasks.GRunnable;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.discord.MessageUtils;
import me.piggypiglet.gary.core.utils.mysql.GiveawayUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import sh.okx.timeapi.TimeAPI;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Giveaway extends GRunnable {
    @Getter private GiveawayBuilder builder;
    @Getter private final CompletableFuture<Message> message = new CompletableFuture<>();
    private String prize;
    private TimeAPI time;
    private GiveawayHandler giveawayHandler;
    private TextChannel channel;

    public Giveaway(@Nonnull final GiveawayBuilder builder, GaryBot garyBot, Message message) {
        this.builder = builder;
        this.prize = builder.getPrize();
        this.time = builder.getTime();
        this.giveawayHandler = garyBot.getInjector().getInstance(GiveawayHandler.class);
        if (message != null) this.message.complete(message);
        channel = garyBot.getJda().getTextChannelById(Constants.GIVEAWAY);

        if (!this.message.isDone()) {
            CompletableFuture<Message> messageFuture = new CompletableFuture<>();

            channel.sendMessage(
                    new EmbedBuilder()
                            .setTitle("Giveaway - " + builder.getAuthor().getName() + "#" + builder.getAuthor().getDiscriminator())
                            .setDescription(channel.getGuild().getPublicRole().getAsMention())
                            .addField("Prize:", prize, false)
                            .addField("Time:", time.getOgTime(), false)
                            .setFooter("Gary v" + GaryBot.getVersion(), garyBot.getJda().getSelfUser().getEffectiveAvatarUrl())
                            .setTimestamp(ZonedDateTime.now())
                            .build()
            ).queue(s -> {
                MessageUtils.addEmoteObjectToMessage(s, Collections.singletonList(MessageUtils.getEmoteObject(builder.getEmote())));
                messageFuture.complete(s);
            });
        }
    }

    @Override
    public void run() {
        Message message;

        try {
            message = this.message.get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        List<User> users = new ArrayList<>();
        message.getReactions().forEach(r -> r.getUsers().forEach(users::add));
        User winner = users.get(new Random().nextInt(users.size()));

        message.delete().queue();
        channel.sendMessage(Lang.getString("commands.giveaway.winner", winner.getAsMention(), prize, builder.getAuthor().getAsMention())).queue();

        giveawayHandler.getGiveaways().remove(message.getIdLong());
        GiveawayUtils.remove(this);
    }

    @Override
    public String toString() {
        long messageId = 0L;

        try {
            if (message.isDone()) {
                messageId = message.get().getIdLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Giveaway(" +
                "prize:" + prize + "," +
                "time:" + time.getOgTime() + "," +
                "emote:" + builder.getEmote() + "," +
                "message:" + messageId + "," +
                ")";
    }
}
