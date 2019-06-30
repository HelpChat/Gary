package me.piggypiglet.gary.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.file.framework.FileConfiguration;
import me.piggypiglet.gary.guice.annotations.Config;
import me.piggypiglet.gary.permission.PermissionMember;
import me.piggypiglet.gary.utils.StringUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class CommandHandler extends ListenerAdapter {
    @Inject @Config private FileConfiguration config;

    @Getter private final List<Command> commands = new ArrayList<>();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw().toLowerCase();
        String prefix = config.getString("command-prefix", ".");

        if (StringUtils.startsWith(message, prefix)) {
            message = message.replaceFirst(prefix, "");

            for (Command command : commands) {
                if (StringUtils.startsWith(message, command.getCommands())) {
                    String[] args = commandSplit(message, command.getCommands());

                    if (new PermissionMember(e.getMember()).hasPermission(command.getPermission())) {
                        command.run(e, args);
                        return;
                    }

                    e.getChannel().sendMessage("no perms").queue();
                }
            }
        }
    }

    private String[] commandSplit(String message, List<String> replacements) {
        final AtomicReference<String> msg = new AtomicReference<>(message);
        replacements.add(config.getString("command-prefix"));
        replacements.forEach(s -> msg.set(msg.get().replaceFirst(s, "")));
        String[] args = msg.get().trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        return args.length == 0 ? new String[] {} : args.length == 1 ? args[0].isEmpty() ? new String[] {} : args : args;
    }
}
