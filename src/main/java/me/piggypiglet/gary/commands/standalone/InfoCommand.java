package me.piggypiglet.gary.commands.standalone;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.utils.misc.GaryInfoUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class InfoCommand extends Command {
    public InfoCommand() {
        super("info");
        options.setDescription("Get info about stuff");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        TextChannel channel = e.getChannel();

        long millisUptime = GaryInfoUtils.getUptime();
        String uptime = String.format("%d days, %d hours, %d minutes, %d seconds",
                TimeUnit.MILLISECONDS.toDays(millisUptime),
                TimeUnit.MILLISECONDS.toHours(millisUptime) % TimeUnit.DAYS.toHours(1),
                TimeUnit.MILLISECONDS.toMinutes(millisUptime) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millisUptime) % TimeUnit.MINUTES.toSeconds(1));
        String version = GaryInfoUtils.getVersion();
        String loadedCommands = String.valueOf(GaryInfoUtils.getLoadedCommands());
        String loadedLoggers = String.valueOf(GaryInfoUtils.getLoadedLoggers());

        if (args.length >= 1) {
            String msg = "";

            switch (args[0]) {
                case "uptime":
                    msg = uptime;
                    break;

                case "version":
                    msg = version;
                    break;

                case "commands":
                    msg = loadedCommands;
            }

            if (!msg.isEmpty()) {
                channel.sendMessage(msg).queue();
            } else {
                channel.sendMessage("Unknown argument `" + args[0] + "`.").queue();
            }

            return;
        }

        channel.sendMessage(new EmbedBuilder()
                .setAuthor("Information", null, e.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .addField("Uptime:", uptime, true)
                .addField("Version:", version, true)
                .addField("Loaded commands:", loadedCommands, true)
                .addField("Loaded loggers:", loadedLoggers, true)
                .setTimestamp(ZonedDateTime.now())
                .build()).queue();
    }
}
