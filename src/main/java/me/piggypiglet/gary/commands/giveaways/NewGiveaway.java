package me.piggypiglet.gary.commands.giveaways;

import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.handlers.misc.GiveawayHandler;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.objects.giveaways.GiveawayBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import sh.okx.timeapi.TimeAPI;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class NewGiveaway extends Command {
    @Inject private GiveawayHandler giveawayHandler;
    @Inject private GaryBot garyBot;

    public NewGiveaway() {
        super("giveaway create");
        options.setRole(Roles.TRUSTED).setDescription("Create a giveaway");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        GiveawayBuilder builder = new GiveawayBuilder();
        builder.setAuthor(e.getAuthor());
        builder.setEmote(args[0]);
        builder.setPrize(args[1]);
        TimeAPI time = new TimeAPI(args[2]);
        builder.setTime(time);
        builder.setTimeLeft(time);
        giveawayHandler.add(builder.build(garyBot));
    }
}
