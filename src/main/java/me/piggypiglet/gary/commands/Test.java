package me.piggypiglet.gary.commands;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.Roles;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Test extends Command {
    public Test() {
        super("test");
        options.setDescription("please don't run").setRole(Roles.ADMIN);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        e.getChannel().sendMessage(
                new EmbedBuilder()
                        .setTitle("How to apply for a role")
                        .setColor(Constants.BLUE)
                        .addField("Developer (Any language*): ", "- Link to Github profile with \nproof that you own it.\n" +
                                "- 2+ Active projects of your \nown.\n" +
                                "- An understanding and attempt \nat following your chosen languages' \nprinciples and conventions.\n" +
                                "\\**No scripting languages*\n\u200C", true)
                        .addField("Server Owner: ", "- Proof of owning a server \nwith 50+ concurrent \nplayers on average.\n\u200C", true)
                        .addField("Artist (Drawing/GFX/Video Creation): ", "- Link to portfolio of past \nprojects (10+ Examples).\n" +
                                "- Proof that you own \nsaid work.\n\u200C", true)
                        .addField("Builder: ", "- Link to portfolio of past \nprojects (5+ Examples).\n" +
                                "- Proof that you own \nsaid work.\n\u200C", true)
                        .addField("How do I prove ownership? ", "Ownership can be proven by simply posting your discord username and discriminator " +
                                "on the service you used as your proof. For example, for developer role, putting your username and discriminator into your GitHub bio.", false)
                        .addField("Format (Case insensitive): ", "```[Role] Role name\n[Proof] Links/screenshots to proof containing your past works and proof of ownership```", false)
                        .setFooter("There is no eta on role request approvals/denials. Please have patience and do not pester staff for a response.", null)
                        .setTimestamp(ZonedDateTime.now())
                        .build()
        ).queue();
    }
}
