package me.piggypiglet.gary.core.utils.misc;

import co.aikar.idb.DB;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.storage.tables.Messages;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class LogUtils {
    @Inject private Messages messages;

    private TextChannel logChannel;

    public void setup(JDA jda) {
        logChannel = jda.getTextChannelById(Constants.LOG);
    }

    public void memberJoin(GuildMemberJoinEvent e) {
        User user = e.getUser();

        MessageEmbed message = new EmbedBuilder()
                .setAuthor("Member Joined", null, user.getAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setColor(Constants.GREEN)
                .setDescription(user.getAsMention() + " " + user.getName() + "#" + user.getDiscriminator())
                .setFooter("ID: " + user.getId(), null)
                .setTimestamp(ZonedDateTime.now())
                .build();

        logChannel.sendMessage(message).queue();
    }

    public void memberLeave(GuildMemberLeaveEvent e) {
        User user = e.getUser();

        //TODO: new member stuff

        MessageEmbed message = new EmbedBuilder()
                .setAuthor("Member Left", null, user.getAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setColor(Constants.RED)
                .setDescription(user.getAsMention() + " " + user.getName() + "#" + user.getDiscriminator())
                .setFooter("ID: " + user.getId(), null)
                .setTimestamp(ZonedDateTime.now())
                .build();

        logChannel.sendMessage(message).queue();
    }

    public void messageDelete(MessageDeleteEvent e) {
        User user = null;
        long message_id = e.getMessageIdLong();
        String message_ = "";

        try {
            if (DB.getFirstColumnAsync("SELECT `discord_id` FROM `gary_messages` WHERE `message_id`=?", message_id).get() != null) {
                user = e.getGuild().getMemberById((Long) DB.getFirstColumnAsync("SELECT `discord_id` FROM `gary_messages` WHERE `message_id`=?", message_id).get()).getUser();
            }

            message_ = (String) DB.getFirstColumnAsync("SELECT `current_message` FROM `gary_messages` WHERE `message_id`=?", message_id).get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (user != null && !message_.isEmpty()) {
            MessageEmbed message = new EmbedBuilder()
                    .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl())
                    .setColor(Constants.RED)
                    .setDescription("**Message sent by " + user.getAsMention() + " deleted in <#" + e.getChannel().getIdLong() + ">**\n" + message_)
                    .setFooter("ID: " + message_id, null)
                    .setTimestamp(ZonedDateTime.now())
                    .build();

            logChannel.sendMessage(message).queue();
            messages.deleteMessage(e.getMessageIdLong());
        }
    }

    public void messageEdit(MessageUpdateEvent e) {
        messages.editMessage(e.getMessage());

        User user = e.getAuthor();
        long message_id = e.getMessageIdLong();
        String current_message = e.getMessage().getContentRaw().length() >= 229 ? e.getMessage().getContentRaw().substring(0, 229) : e.getMessage().getContentRaw();
        String previous_message = "";

        if (current_message.length() == 229) {
            current_message = current_message + "...";
        }

        try {
            previous_message = (String) DB.getFirstColumnAsync("SELECT `previous_message` FROM `gary_messages` WHERE `message_id`=?", message_id).get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!previous_message.isEmpty()) {
            MessageEmbed.Field before = new MessageEmbed.Field("Before", previous_message, false);
            MessageEmbed.Field after = new MessageEmbed.Field("After", current_message, false);

            MessageEmbed message = new EmbedBuilder()
                    .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl())
                    .setColor(Constants.BLUE)
                    .setDescription("**Message edited in <#" + e.getChannel().getIdLong() + ">**")
                    .addField(before)
                    .addField(after)
                    .setFooter("User ID: " + user.getIdLong(), null)
                    .setTimestamp(ZonedDateTime.now())
                    .build();

            logChannel.sendMessage(message).queue();
        }
    }

    public void voiceJoin(GuildVoiceJoinEvent e) {
        User user = e.getMember().getUser();

        MessageEmbed message = new EmbedBuilder()
                .setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getEffectiveAvatarUrl())
                .setColor(Constants.BLUE)
                .setDescription(user.getAsMention() + " **joined voice channel <#" + e.getChannelJoined().getIdLong() + ">**")
                .setFooter("ID: " + e.getChannelJoined().getIdLong(), null)
                .setTimestamp(ZonedDateTime.now())
                .build();

        logChannel.sendMessage(message).queue();
    }
}
