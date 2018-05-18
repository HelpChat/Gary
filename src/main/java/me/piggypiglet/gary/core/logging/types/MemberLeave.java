package me.piggypiglet.gary.core.logging.types;

import me.piggypiglet.gary.core.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MemberLeave extends Logger {
    public MemberLeave() {
        super(EventsEnum.MEMBER_LEAVE);
    }

    @Override
    protected MessageEmbed send() {
        if (getOther()[0] instanceof User) {
            User user = (User) getOther()[0];

            return new EmbedBuilder()
                    .setAuthor("Member Left", null, user.getAvatarUrl())
                    .setThumbnail(user.getEffectiveAvatarUrl())
                    .setColor(Constants.RED)
                    .setDescription(user.getAsMention() + " " + user.getName() + "#" + user.getDiscriminator())
                    .setFooter("ID: " + user.getId(), null)
                    .setTimestamp(ZonedDateTime.now())
                    .build();
        }

        return null;
    }
}
