package me.piggypiglet.gary.core.storage.tables;

import co.aikar.idb.DB;
import net.dv8tion.jda.core.entities.Message;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class Messages {
    public void addMessage(Message message) {
        long discord_id = message.getAuthor().getIdLong();
        long message_id = message.getIdLong();
        String message_ = message.getContentRaw().length() >= 229 ? message.getContentRaw().substring(0, 229) : message.getContentRaw();

        if (message_.length() == 229) {
            message_ = message_ + "...";
        }

        try {
            DB.executeInsert("INSERT INTO `gary_messages` (`id`, `discord_id`, `message_id`, `previous_message`, `current_message`) VALUES ('0', ?, ?, ?, ?);", discord_id, message_id, message_, message_);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editMessage(Message message) {
        long message_id = message.getIdLong();
        String message_ = message.getContentRaw().length() >= 229 ? message.getContentRaw().substring(0, 229) : message.getContentRaw();

        if (message_.length() == 229) {
            message_ = message_ + "...";
        }

        try {
            if (DB.getFirstColumnAsync("SELECT * FROM `gary_messages` WHERE `message_id`=?", message_id).get() != null) {
                String previousMessage = DB.getFirstColumnAsync("SELECT `current_message` FROM `gary_messages` WHERE `message_id`=?", message_id).get().toString();
                DB.executeUpdateAsync("UPDATE `gary_messages` SET `previous_message`=?, `current_message`=? WHERE `message_id`=?", previousMessage, message_, message_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(long message_id) {
        try {
            if (DB.getFirstColumnAsync("SELECT * FROM `gary_messages` WHERE `message_id`=?", message_id).get() != null) {
                DB.executeUpdateAsync("DELETE FROM `gary_messages` WHERE `message_id`=?", message_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
