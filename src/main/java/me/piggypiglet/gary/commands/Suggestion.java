package me.piggypiglet.gary.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.util.MessageUtil;
import me.piggypiglet.gary.core.util.TimeUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Suggestion extends Command {
    @Inject private TimeUtil tutil;
    @Inject private MessageUtil mutil;

    public Suggestion() {
        super("?suggestion");

        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        //TODO: Temp solution, change to webhook soonTM or github magic (automatically edit issue).
        if (args[0].length() == 0) {
            e.getChannel().sendMessage("Invalid suggestion").queue();
            return;
        }
        MessageEmbed.Field field = new MessageEmbed.Field("Suggestion:", mutil.arrayToString(args), true);
        MessageEmbed embed = new EmbedBuilder()
                .setAuthor(e.getMessage().getAuthor().getName() + "#" + e.getMessage().getAuthor().getDiscriminator())
                .addField(field)
                .setFooter("Created at " + tutil.getTime() + " in #" + e.getChannel().getName(), null)
                .build();
        e.getGuild().getTextChannelById(Constants.PIG).sendMessage(embed).queue();
        e.getChannel().sendMessage("Suggestion successfully sent!").complete().delete().queueAfter(12, TimeUnit.SECONDS);
    }
}
