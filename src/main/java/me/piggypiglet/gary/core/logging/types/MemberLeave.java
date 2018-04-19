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
public class MemberLeave extends Logger {
    public MemberLeave() {
        super(LogType.MEMBER_LEAVE);
    }

    @Override
    protected MessageEmbed send() {
        User user = getUser();

        //TODO: new member stuff

        return new EmbedBuilder()
                .setAuthor("Member Left", null, user.getAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setColor(Constants.RED)
                .setDescription(user.getAsMention() + " " + user.getName() + "#" + user.getDiscriminator())
                .setFooter("ID: " + user.getId(), null)
                .setTimestamp(ZonedDateTime.now())
                .build();
    }
}
