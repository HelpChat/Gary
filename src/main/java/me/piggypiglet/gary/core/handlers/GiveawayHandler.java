package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.storage.mysql.tables.Giveaways;
import me.piggypiglet.gary.core.tasks.GiveawayTask;
import me.piggypiglet.gary.core.tasks.RunTasks;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class GiveawayHandler extends ListenerAdapter {
    @Inject private RunTasks runTasks;
    @Inject private Giveaways giveaways;

    private Map<Long, AbstractMap.Entry<GiveawayTask, Long>> giveawaysMap;
    private List<GiveawayTask> liveTasks;

    public GiveawayHandler() {
        giveawaysMap = new HashMap<>();
        liveTasks = new ArrayList<>();
    }

    public Map<Long, AbstractMap.Entry<GiveawayTask, Long>> getGiveaways() {
        return giveawaysMap;
    }

    public void removeGiveaway(long messageId) {
        giveawaysMap.remove(messageId);

        List<GiveawayTask> removeableItems = new ArrayList<>();

        for (GiveawayTask task : liveTasks) {
            if (task.getMessageId() == messageId) {
                removeableItems.add(task);
            }
        }

        liveTasks.removeAll(removeableItems);
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        if (e.getChannel().getIdLong() == Constants.GIVEAWAY_CHANNEL) {
            long messageId = e.getMessageIdLong();

            if (messageId == Constants.GIVEAWAY_MESSAGE) {
                Guild guild = e.getGuild();

                guild.getController().addSingleRoleToMember(e.getMember(), guild.getRoleById(Constants.GIVEAWAY_ROLE)).queue();
            } else {
                User user = e.getUser();

                if (!user.isBot()) {
                    if (giveawaysMap.containsKey(messageId)) {
                        giveaways.addUser(messageId, user.getIdLong());
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent e) {
        if (e.getChannel().getIdLong() == Constants.GIVEAWAY_CHANNEL) {
            long messageId = e.getMessageIdLong();

            if (messageId == Constants.GIVEAWAY_MESSAGE) {
                Guild guild = e.getGuild();

                guild.getController().removeSingleRoleFromMember(e.getMember(), guild.getRoleById(Constants.GIVEAWAY_ROLE)).queue();
            } else {
                User user = e.getUser();

                if (!user.isBot()) {
                    if (giveawaysMap.containsKey(messageId)) {
                        giveaways.removeUser(messageId, user.getIdLong());
                    }
                }
            }
        }
    }

    public void update(TextChannel channel) {
        if (!giveawaysMap.isEmpty()) {
            List<AbstractMap.Entry<GiveawayTask, Long>> tasks = new ArrayList<>(giveawaysMap.values());

            for (AbstractMap.Entry<GiveawayTask, Long> task : tasks) {
                GiveawayTask giveawayTask = task.getKey();

                if (!liveTasks.contains(giveawayTask)) {
                    liveTasks.add(giveawayTask);
                    runTasks.newTask(giveawayTask, task.getValue(), false);
                }
            }
        } else {
            giveaways.populateGiveaways(channel);

            if (!giveawaysMap.isEmpty()) {
                update(channel);
            }
        }
    }
}
