package me.piggypiglet.gary.commands.server;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.handlers.CommandHandler;
import me.piggypiglet.gary.core.objects.GFile;
import me.piggypiglet.gary.core.objects.Version;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class Info extends Command {
    @Inject private Version version;
    @Inject private CommandHandler commandHandler;
    @Inject private GFile gFile;

    public Info() {
        super("?info/?serverinfo");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        List<Member> membersOnline = new ArrayList<>();
        e.getGuild().getMembers().forEach(member -> { if (member.getOnlineStatus() != OnlineStatus.OFFLINE) membersOnline.add(member); });

        MessageEmbed.Field helpchat = new MessageEmbed.Field("HelpChat:", "Info regarding the discord server\n\u200C\u200C", false);
        MessageEmbed.Field users = new MessageEmbed.Field("Users online:", membersOnline.size() + "/" + e.getGuild().getMembers().size() + "\n\u200C\u200C", true);

        MessageEmbed.Field gary = new MessageEmbed.Field("Gary:", "Info about gary himself\n\u200C\u200C", false);
        MessageEmbed.Field uptime = new MessageEmbed.Field("Uptime:", TimeUnit.MILLISECONDS.toHours(ManagementFactory.getRuntimeMXBean().getUptime()) + " hours uptime.", true);
        MessageEmbed.Field commands = new MessageEmbed.Field("Loaded Commands:", commandHandler.getCommands().size() + " loaded commands.", true);
        MessageEmbed.Field configs = new MessageEmbed.Field("Loaded Files:", gFile.getgFiles().size() + " loaded files.", true);

        MessageEmbed message = new EmbedBuilder()
                .setTitle("Info")
                .setThumbnail(e.getGuild().getIconUrl())
                .addField(helpchat)
                .addField(users)
                .addField(gary)
                .addField(uptime)
                .addField(commands)
                .addField(configs)
                .setFooter("Gary v" + version.getVersion(), e.getJDA().getSelfUser().getAvatarUrl())
                .build();

        e.getChannel().sendMessage(message).queue();
    }
}
