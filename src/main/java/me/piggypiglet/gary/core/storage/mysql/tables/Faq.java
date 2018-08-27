package me.piggypiglet.gary.core.storage.mysql.tables;

import co.aikar.idb.DB;
import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.entities.User;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Faq {
    @Inject private GaryBot garyBot;

    public void addFaq(String identifier, String message, long userId) {
        Thread thread = new Thread(() -> {
            try {
                DB.executeInsert("INSERT INTO `gary_faq` (`id`, `identifier`, `message`, `user_id`) VALUES ('0', ?, ?, ?);", identifier, message, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setName("MySQL");
        thread.start();
    }

    public void removeFaq(String identifier) {
        DB.executeUpdateAsync("DELETE FROM `gary_faq` WHERE `identifier`=?;", identifier);
    }

    public String getFaq(String identifier) {
        try {
            return DB.getFirstRowAsync("SELECT * FROM `gary_faq` WHERE `identifier`=?;", identifier).get().getString("message");
        } catch (Exception e) {
            return "Unknown faq";
        }
    }

    public String getUser(String identifier) {
        try {
            User user = garyBot.getJda().getUserById(DB.getFirstRowAsync("SELECT * FROM `gary_faq` WHERE `identifier`=?", identifier).get().getLong("user_id"));
            return user.getName() + "#" + user.getDiscriminator();
        } catch (Exception e) {
            User user = garyBot.getJda().getUserById(Constants.GARY);
            return user.getName() + "#" + user.getDiscriminator();
        }
    }
}
