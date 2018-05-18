package me.piggypiglet.gary.core.storage.mysql.tables;

import co.aikar.idb.DB;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Messages {
    public void addMessage(Message message) {
        long discordId = message.getAuthor().getIdLong();
        long messageId = message.getIdLong();
        String message_ = message.getContentRaw().length() >= 229 ? message.getContentRaw().substring(0, 229) : message.getContentRaw();

        if (message_.length() == 229) {
            message_ = message_ + "...";
        }

        try {
            DB.executeInsert("INSERT INTO `gary_messages` (`id`, `discord_id`, `message_id`, `previous_message`, `current_message`) VALUES ('0', ?, ?, ?, ?);", discordId, messageId, message_, message_);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editMessage(Message message) {
        long messageId = message.getIdLong();
        String message_ = message.getContentRaw().length() >= 229 ? message.getContentRaw().substring(0, 229) : message.getContentRaw();

        if (message_.length() == 229) {
            message_ = message_ + "...";
        }

        try {
            if (DB.getFirstColumnAsync("SELECT * FROM `gary_messages` WHERE `message_id`=?", messageId).get() != null) {
                String previousMessage = (String) DB.getFirstColumnAsync("SELECT `current_message` FROM `gary_messages` WHERE `message_id`=?", messageId).get();

                DB.executeUpdateAsync("UPDATE `gary_messages` SET `previous_message`=?, `current_message`=? WHERE `message_id`=?", previousMessage, message_, messageId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(long messageId) {
        try {
            if (DB.getFirstColumnAsync("SELECT * FROM `gary_messages` WHERE `message_id`=?", messageId).get() != null) {
                DB.executeUpdateAsync("DELETE FROM `gary_messages` WHERE `message_id`=?", messageId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser(Guild guild, long messageId) {
        User user = null;

        try {
            Object user_ = DB.getFirstColumnAsync("SELECT `discord_id` FROM `gary_messages` WHERE `message_id`=?", messageId).get();

            if (user_ != null) user = guild.getMemberById((Long) user_).getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public String getMessage(long messageId) {
        String message = "";

        try {
            Object message_ = DB.getFirstColumnAsync("SELECT `current_message` FROM `gary_messages` WHERE `message_id`=?", messageId).get();

            if (message_ != null) message = (String) message_;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    public String getPreviousMessage(long messageId) {
        String message = "";

        try {
            Object message_ = DB.getFirstColumnAsync("SELECT `previous_message` FROM `gary_messages` WHERE `message_id`=?", messageId).get();

            if (message_ != null) message = (String) message_;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}
