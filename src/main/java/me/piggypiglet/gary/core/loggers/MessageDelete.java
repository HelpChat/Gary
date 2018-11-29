package me.piggypiglet.gary.core.loggers;

import me.piggypiglet.gary.core.framework.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.audit.ActionType;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MessageDelete extends Logger {
    public MessageDelete() {
        super(EventsEnum.MESSAGE_DELETE);
    }

    @Override
    protected MessageEmbed send() {
        User user = users.get(0);
        TextChannel channel = channels.get(0);
        String string = strings.get(0);
        Long aLong = longs.get(0);
        final AtomicReference<String> deleter = new AtomicReference<>("self");

        // TODO: Fix MySQL making blank rows instead of deleting rows.

        guild.getAuditLogs().type(ActionType.MESSAGE_DELETE).queue(l -> {
            for (AuditLogEntry entry : l) {
                System.out.println();

                if (entry.getTargetIdLong() == user.getIdLong() && entry.getCreationTime().isAfter(OffsetDateTime.now().minus(Duration.ofMinutes(1)))) {
                    deleter.set(Objects.requireNonNull(entry.getUser()).getAsMention());
                    break;
                }
            }
        });

        return new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setColor(Constants.RED)
                .setDescription("**Message sent by " + user.getAsMention() + " deleted in " + channel.getAsMention() + " by " + deleter + ".**\n" + string)
                .setFooter("ID: " + aLong, null)
                .setTimestamp(ZonedDateTime.now())
                .build();
    }
}
