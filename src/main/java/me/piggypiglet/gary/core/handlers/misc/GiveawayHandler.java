package me.piggypiglet.gary.core.handlers.misc;

import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.objects.tasks.Task;
import me.piggypiglet.gary.core.objects.tasks.tasks.Giveaway;
import me.piggypiglet.gary.core.utils.mysql.GiveawayUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class GiveawayHandler extends GEvent {
    @Getter private final Map<Long, Giveaway> giveaways = new ConcurrentHashMap<>();

    public GiveawayHandler() {
        super(EventsEnum.MESSAGE_REACTION_ADD, EventsEnum.MESSAGE_REACTION_REMOVE);
    }

    @Override
    protected void execute(GenericEvent event) {
        GenericGuildMessageReactionEvent e = (GenericGuildMessageReactionEvent) event;

        if (!e.getUser().isBot() && e.getReactionEmote().getName().equalsIgnoreCase(Constants.THUMBSUP) && e.getMessageIdLong() == Constants.GIVEAWAY_MESSAGE) {
            Guild guild = e.getGuild();
            Role role = guild.getRoleById(Constants.GIVEAWAY_ROLE);

            switch (EventsEnum.fromEvent(event)) {
                case MESSAGE_REACTION_ADD:
                    guild.getController().addRolesToMember(e.getMember(), role).queue();
                    break;

                case MESSAGE_REACTION_REMOVE:
                    guild.getController().removeRolesFromMember(e.getMember(), role).queue();
                    break;
            }
        }
    }

    public void add(Giveaway giveaway) {
        //noinspection StatementWithEmptyBody
        while (!giveaway.getMessage().isDone()) {}

        try {
            giveaways.put(giveaway.getMessage().get().getIdLong(), giveaway);
            Task.scheduleAsync(giveaway, giveaway.getBuilder().getTimeLeft().getSeconds(), TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populate() {
        GiveawayUtils.getGiveaways().forEach(this::add);
    }
}
