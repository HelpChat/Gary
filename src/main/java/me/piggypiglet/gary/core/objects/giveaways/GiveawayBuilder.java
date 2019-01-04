package me.piggypiglet.gary.core.objects.giveaways;

import lombok.Getter;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.tasks.tasks.Giveaway;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import sh.okx.timeapi.TimeAPI;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GiveawayBuilder {
    @Getter private String prize;
    @Getter private TimeAPI time;
    @Getter private TimeAPI timeLeft;
    @Getter private String emote;
    @Getter private User author;

    public GiveawayBuilder setPrize(String prize) {
        this.prize = prize;
        return this;
    }

    public GiveawayBuilder setTime(TimeAPI time) {
        this.time = time;
        return this;
    }

    public GiveawayBuilder setTimeLeft(TimeAPI timeLeft) {
        this.timeLeft = timeLeft;
        return this;
    }

    public GiveawayBuilder setEmote(String emote) {
        this.emote = emote;
        return this;
    }

    public GiveawayBuilder setAuthor(User author) {
        this.author = author;
        return this;
    }

    public Giveaway build(GaryBot garyBot) {
        return new Giveaway(this, garyBot, null);
    }

    public Giveaway build(GaryBot garyBot, Message message) {
        return new Giveaway(this, garyBot, message);
    }
}