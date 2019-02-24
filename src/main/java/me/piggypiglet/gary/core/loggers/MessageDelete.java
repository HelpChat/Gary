package me.piggypiglet.gary.core.loggers;

import me.piggypiglet.gary.core.framework.logging.Logger;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MessageDelete extends Logger {
    public MessageDelete() {
        super(EventsEnum.MESSAGE_DELETE);
    }

    @Override
    protected MessageEmbed send() throws Exception {
        User user = users.get(0);
        TextChannel channel = textChannels.get(0);
        String string = strings.get(0);
        Long aLong = longs.get(0);
        CompletableFuture<String> deleter = new CompletableFuture<>();

        guild.retrieveAuditLogs().limit(1).type(ActionType.MESSAGE_DELETE).queue(l -> {
            for (AuditLogEntry entry : l) {
                User entryUser = entry.getUser();

                if (entryUser != null && entry.getTargetIdLong() == user.getIdLong() && entry.getUser().getIdLong() != user.getIdLong()) {
                    deleter.complete(entry.getUser().getAsMention());
                } else {
                    deleter.complete("self");
                }
            }
        });

        //noinspection StatementWithEmptyBody
        while (!deleter.isDone()) {}

        return new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setColor(Constants.RED)
                .setDescription("**Message sent by " + user.getAsMention() + " deleted in " + channel.getAsMention() + " by " + deleter.get() + ".**\n" + string)
                .setFooter("ID: " + aLong, null)
                .setTimestamp(ZonedDateTime.now())
                .build();
    }
}
