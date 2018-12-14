package me.piggypiglet.gary.commands.standalone;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.handlers.chat.CommandHandler;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.storage.file.GFile;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.discord.RoleUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class HelpCommand extends Command {
    @Inject private CommandHandler commandHandler;
    @Inject private GFile gFile;

    public HelpCommand() {
        super("help");
        options.setDescription("Shows this menu.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor("Gary Command Help Menu", null, e.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setTimestamp(ZonedDateTime.now())
                .setFooter("Gary v" + gFile.getFileConfiguration("embed").getString("version"), null);

        e.getChannel().sendMessage(
                builder.addField("Commands:", String.join("\n", getFormattedCommandsForRole(RoleUtils.getRole(e.getMember()))), false).build()
        ).queue();
    }

    private List<String> getFormattedCommandsForRole(Roles role) {
        return commandHandler.getCommands().stream().filter(c -> Roles.isRoleOrUnder(c.getAllowedRole(), role)).distinct()
                .map(c -> "**" + Lang.getStringList("commands.prefix").get(0) + c.getCommands().get(0) + "** - " + c.getDescription())
                .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }
}
