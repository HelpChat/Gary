package me.piggypiglet.gary.core.logging.types;

import me.piggypiglet.gary.core.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.LogType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class MemberJoin extends Logger {
    public MemberJoin() {
        super(LogType.MEMBER_JOIN);
    }

    @Override
    protected MessageEmbed send() {
        User user = getUser();

        return new EmbedBuilder()
                .setAuthor("Member Joined", null, user.getAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setColor(Constants.GREEN)
                .setDescription(user.getAsMention() + " " + user.getName() + "#" + user.getDiscriminator())
                .setFooter("ID: " + user.getId(), null)
                .setTimestamp(ZonedDateTime.now())
                .build();
    }
}
