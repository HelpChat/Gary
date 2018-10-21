package me.piggypiglet.gary.core.loggers;

import me.piggypiglet.gary.core.framework.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MemberJoin extends Logger {
    public MemberJoin() {
        super(EventsEnum.MEMBER_JOIN);
    }

    @Override
    protected MessageEmbed send() {
        return new EmbedBuilder()
                .setAuthor("Member Joined", null, user.getEffectiveAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setColor(Constants.GREEN)
                .setDescription(user.getAsMention() + " " + user.getName() + "#" + user.getDiscriminator())
                .setFooter("ID: " + user.getId(), null)
                .setTimestamp(ZonedDateTime.now())
                .build();
    }
}
