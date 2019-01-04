package me.piggypiglet.gary.core.framework.commands;

import lombok.Getter;
import me.piggypiglet.gary.core.objects.enums.Roles;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class Command {
    @Getter private final List<String> commands = new ArrayList<>();

    protected final Options options = new Options();
    @Getter private Roles allowedRole = Roles.EVERYBODY;
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

        public Options setRole(Roles allowedRole) {
            Command.this.allowedRole = allowedRole;
            return this;
        }

        public Options setDescription(String description) {
            Command.this.description = description;
            return this;
        }
    }
}
