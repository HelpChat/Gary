package me.piggypiglet.gary.core.utils.mysql;

import co.aikar.idb.DB;
import net.dv8tion.jda.core.entities.Message;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MessageUtils {
    public static void addMessage(Message message) {
        long userID = message.getAuthor().getIdLong();
        long messageID = message.getIdLong();
        String messageContent = message.getContentRaw().length() >= 229 ? message.getContentRaw().substring(0, 229) + "..." : message.getContentRaw();

        MySQLUtils.add("gary_messages", new String[] {"user_id", "message_id", "previous_message", "current_message"}, userID, messageID, messageContent, messageContent);
    }

    public static void editMessage(Message message) {
        long userID = message.getAuthor().getIdLong();
        long messageID = message.getIdLong();
        String messageContent = message.getContentRaw().length() >= 229 ? message.getContentRaw().substring(0, 229) + "..." : message.getContentRaw();

        try {
            String previousMessage = (String) DB.getFirstColumnAsync("SELECT `current_message` FROM `gary_messages` WHERE `message_id`=?", messageID).get();

            if (previousMessage != null) {
                DB.executeUpdateAsync("UPDATE `gary_messages` SET `previous_message`=?, `current_message`=?, WHERE `message_id`=?", previousMessage, messageContent, messageID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteMessage(long messageID) {
        try {
            if (DB.getFirstColumnAsync("SELECT * FROM `gary_messages` WHERE `message_id`=?", messageID).get() != null) {
                DB.executeUpdateAsync("DELETE FROM `gary_messages` WHERE `message_id`=?", messageID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMessage(long messageID) {
        return getMessage(messageID, "current_message");
    }

    public static String getPreviousMessage(long messageID) {
        return getMessage(messageID, "previous_message");
    }

    private static String getMessage(long messageID, String type) {
        String result = "null";

        try {
            Object message = DB.getFirstColumnAsync("SELECT ? FROM `gary_messages` WHERE `message_id`=?", type, messageID).get();

            if (message != null) result = (String) message;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}