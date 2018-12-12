package me.piggypiglet.gary.commands.punishment.ban;

import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.storage.file.Lang;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class AddBan extends Command {
    public AddBan() {
        super("ban");
        options.setRole(Roles.TRUSTED).save();
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            Member member;
            String reason = "";

            try {
                member = e.getGuild().getMemberById(args[0]);
            } catch (Exception ex) {
                e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "ban <user id> [reason]")).queue();
                return;
            }

            if (args.length >= 2) {
                reason = args[1];
            }

            e.getGuild().getController().ban(member, 0, reason).queue();
            e.getChannel().sendMessage(Lang.getString("commands.punishment.ban.add.success", member.getAsMention())).queue();
        } else {
            e.getChannel().sendMessage(Lang.getString("commands.incorrect-usage", "ban <user id> [reason]")).queue();
        }
    }
}
