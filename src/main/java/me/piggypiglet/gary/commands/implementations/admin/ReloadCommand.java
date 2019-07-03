package me.piggypiglet.gary.commands.implementations.admin;

import com.google.inject.Inject;
import me.piggypiglet.gary.commands.Command;
import me.piggypiglet.gary.file.FileManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.stream.Stream;

import static me.piggypiglet.gary.file.implementations.json.types.Lang.RELOAD;
import static me.piggypiglet.gary.file.implementations.json.types.Lang.getMessage;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class ReloadCommand extends Command {
    @Inject private FileManager fileManager;

    public ReloadCommand() {
        super("reload");
        options.setPermission("reload");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        Stream.of("config", "lang", "roles").forEach(fileManager::update);
        e.getChannel().sendMessage(getMessage(RELOAD)).queue();
    }
}
