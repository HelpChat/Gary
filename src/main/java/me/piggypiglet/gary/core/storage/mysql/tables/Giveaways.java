package me.piggypiglet.gary.core.storage.mysql.tables;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.handlers.GiveawayHandler;
import me.piggypiglet.gary.core.tasks.GiveawayTask;
import me.piggypiglet.gary.core.tasks.RunTasks;
import net.dv8tion.jda.core.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class Giveaways {
    @Inject private GiveawayHandler giveawayHandler;
    @Inject private RunTasks runTasks;

    private Map<Long, Integer> giveawayIds;
    private Logger logger;

    public Giveaways() {
        giveawayIds = new HashMap<>();
        logger = LoggerFactory.getLogger("Giveaways");
    }

    @SuppressWarnings("unchecked")
    public void newGiveaway(TextChannel channel, Long messageId, Long time, String user, GiveawayTask giveawayTask) {
        AbstractMap.Entry entry = new AbstractMap.SimpleEntry<>(giveawayTask, time);

        Thread thread = new Thread(() -> {
            try {
                DB.executeInsert("INSERT INTO `gary_giveaways` (`id`, `message_id`, `time`) VALUES ('0', ?, ?);", messageId, time);
                giveawayIds.put(messageId, DB.getFirstColumn("SELECT `id` FROM `gary_giveaways` WHERE message_id=?;", messageId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setName("MySQL");
        thread.start();

        giveawayHandler.getGiveaways().put(messageId, entry);
        giveawayHandler.update(channel);
        logger.info("New giveaway created by " + user);
    }

    public void addUser(long messageId, long userId) {
        Thread thread = new Thread(() -> {
            try {
                DB.executeInsert("INSERT INTO `gary_giveaways_users` (`id`, `giveaway_id`, `user_id`) VALUES ('0', ?, ?);", giveawayIds.get(messageId), userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setName("MySQL");
        thread.start();
    }

    public void removeUser(long messageId, long userId) {
        DB.executeUpdateAsync("DELETE * FROM `gary_giveaways_users` WHERE (message_id=?) and (user_id=?);", messageId, userId);
    }

    public List<Long> getUsers(long messageId) {
        List<Long> results = new ArrayList<>();

        try {
            List<DbRow> rows = DB.getResultsAsync("SELECT `user_id` FROM `gary_giveaways_users` WHERE giveaway_id=?;", giveawayIds.get(messageId)).get();

            for (DbRow row : rows) {
                results.add(row.getLong("user_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    public void populateGiveaways(TextChannel channel) {
        try {
            List<DbRow> rows = DB.getResultsAsync("SELECT * FROM `gary_giveaways`;").get();

            for (DbRow row : rows) {
                long messageId = row.getLong("message_id");
                long time = row.getLong("time");
                String prize = row.getString("prize");

                giveawayHandler.getGiveaways().put(messageId, new AbstractMap.SimpleEntry<>(new GiveawayTask(this, messageId, channel, prize), time));
            }
        } catch (NullPointerException npe) {
            logger.info("Giveaway table empty, not importing any giveaways.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelGiveaway(GiveawayTask task, long messageId, TextChannel channel) {
        giveawayHandler.removeGiveaway(messageId);
        runTasks.killTask(task);

        DB.executeUpdateAsync("DELETE * FROM `gary_giveaways_users` WHERE giveaway_id=?;", giveawayIds.get(messageId));
        DB.executeUpdateAsync("DELETE * FROM `gary_giveaways` WHERE message_id=?;", messageId);

        channel.getMessageById(messageId).complete().delete().queue();
    }
}
