package me.piggypiglet.gary.commands.server;

import com.google.inject.Inject;
import me.piggypiglet.gary.ChatReaction;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.handlers.chat.CommandHandler;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.storage.json.GFile;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Info extends Command {
    @Inject private CommandHandler commandHandler;
    @Inject private GFile gFile;
    @Inject private ChatReaction chatReaction;

    public Info() {
        super("?info/?serverinfo/?stats", "Get info about the discord server.", true);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        List<Member> totalOnlineList = new ArrayList<>(), membersList = new ArrayList<>(), membersOnlineList = new ArrayList<>();
        List<Member> botsList = new ArrayList<>(), botsOnlineList = new ArrayList<>();

        e.getGuild().getMembers().forEach(member -> {
            if (member.getOnlineStatus() != OnlineStatus.OFFLINE && !member.getUser().isBot()) membersOnlineList.add(member);
            if (!member.getUser().isBot()) membersList.add(member);
            if (member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getUser().isBot()) botsOnlineList.add(member);
            if (member.getUser().isBot()) botsList.add(member);
            if (member.getOnlineStatus() != OnlineStatus.OFFLINE) totalOnlineList.add(member);
        });

        String total = totalOnlineList.size() + "/" + e.getGuild().getMembers().size();
        String members = membersOnlineList.size() + "/" + membersList.size();
        String bots = botsOnlineList.size() + "/" + botsList.size();

        MessageEmbed.Field helpchat = new MessageEmbed.Field("HelpChat:", "Info regarding the discord server\n\u200C", false);
        MessageEmbed.Field users = new MessageEmbed.Field("Users online:", "Total: " + total + "\n\u200CMembers: " + members + "\n\u200C\u200CBots: " + bots + "\n\u200C", true);

        MessageEmbed.Field gary = new MessageEmbed.Field("Gary:", "Info about gary himself\n\u200C", false);
        MessageEmbed.Field uptime = new MessageEmbed.Field("Uptime:", TimeUnit.MILLISECONDS.toHours(ManagementFactory.getRuntimeMXBean().getUptime()) + " hours uptime.", true);
        MessageEmbed.Field commands = new MessageEmbed.Field("Loaded Commands:", commandHandler.getCommands().size() + " loaded commands.", true);
        MessageEmbed.Field configs = new MessageEmbed.Field("Loaded Files:", gFile.getItemMaps().size() + " loaded files.", true);
        MessageEmbed.Field words = new MessageEmbed.Field("Loaded Words:", Objects.requireNonNull(chatReaction.getWords()).size() + " loaded words.", true);

        MessageEmbed message = new EmbedBuilder()
                .setTitle("Info")
                .setThumbnail(e.getGuild().getIconUrl())
                .addField(helpchat)
                .addField(users)
                .addField(gary)
                .addField(uptime)
                .addField(commands)
                .addField(configs)
                .addField(words)
                .setFooter("Gary v" + Constants.VERSION, e.getJDA().getSelfUser().getAvatarUrl())
                .build();

        e.getChannel().sendMessage(message).queue();
    }
}
