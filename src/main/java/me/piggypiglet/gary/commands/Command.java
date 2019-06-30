package me.piggypiglet.gary.commands;

import lombok.Getter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public abstract class Command {
    @Getter private final List<String> commands = new ArrayList<>();

    protected final Options options = new Options();
    @Getter private String permission = "null";
    @Getter private String description = "null";

    protected Command(String... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    protected abstract void execute(GuildMessageReceivedEvent e, String[] args);

    public void run(GuildMessageReceivedEvent e, String[] args) {
        execute(e, args);
    }

    protected class Options {
        private Options() {}

        public Options setPermission(String permission) {
            Command.this.permission = permission;
            return this;
        }

        public Options setDescription(String description) {
            Command.this.description = description;
            return this;
        }
    }
}