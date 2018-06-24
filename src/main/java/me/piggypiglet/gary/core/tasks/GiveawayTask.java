package me.piggypiglet.gary.core.tasks;

import me.piggypiglet.gary.core.storage.mysql.tables.Giveaways;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GiveawayTask extends TimerTask {
    private Giveaways giveaways;
    private final long messageId;
    private TextChannel channel;
    private String prize;

    public GiveawayTask(Giveaways giveaways, long messageId, TextChannel channel, String prize) {
        this.giveaways = giveaways;
        this.messageId = messageId;
        this.channel = channel;
        this.prize = prize;
    }

    public long getMessageId() {
        return messageId;
    }

    @Override
    public void run() {
        Random random = new Random();
        List<Long> users = giveaways.getUsers(messageId);

        long user = users.get(random.nextInt(users.size()));
        channel.sendMessage("<@" + user + "> has won " + prize).queue();
        giveaways.cancelGiveaway(this, messageId, channel);
    }
}
