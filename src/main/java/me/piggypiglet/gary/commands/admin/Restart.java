package me.piggypiglet.gary.commands.admin;

import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class Restart extends Command {
    public Restart() {
        super("?restart");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (e.getAuthor().getIdLong() == Constants.PIGGYPIGLET) {
            try {
                Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "/home/gary/test.sh"}, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
