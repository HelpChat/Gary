package me.piggypiglet.gary.core.objects.giveaways;

import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import sh.okx.timeapi.TimeAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GiveawayBuilder {
    private JDA jda;
    private String prize;
    private TimeAPI time;

    private Emote emote;
    private String unicode;

    public GiveawayBuilder setJDA(JDA jda) {
        this.jda = jda;
        return this;
    }

    public GiveawayBuilder setPrize(String prize) {
        this.prize = prize;
        return this;
    }

    public GiveawayBuilder setTime(String time) {
        this.time = new TimeAPI(time);
        return this;
    }

    public GiveawayBuilder setEmoji(String emoji) {
        String tempEmoji = emoji.replaceAll("\\D+", "");
        List<String> emojiIds = new ArrayList<>();
        jda.getGuildById(Constants.HELP_CHAT).getEmotes().stream().map(Emote::getId).forEach(emojiIds::add);

        if (emojiIds.contains(tempEmoji)) {
            this.emote = jda.getEmoteById(tempEmoji);
        } else {
            this.unicode = emoji;
        }

        return this;
    }

    public TimeAPI getTime() {
        return time;
    }

    public Message build() {
        StringBuilder builder = new StringBuilder();

        Stream.of(
                "<@&457243404485525515>",
                "Prize: " + prize,
                "Time: " + time.getOgTime(),
                "\nReact to this message to enter the giveaway."
        ).forEach(str -> builder.append("\n").append(str));

        Message message = jda.getTextChannelById(Constants.GIVEAWAY_CHANNEL)
                .sendMessage(builder.toString()).complete();
        if (emote != null) {
            message.addReaction(emote).queue();
        } else if (unicode != null) {
            message.addReaction(unicode).queue();
        }

        return message;
    }
}
