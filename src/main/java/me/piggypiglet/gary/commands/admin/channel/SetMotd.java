package me.piggypiglet.gary.commands.admin.channel;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.admin.RoleUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public final class SetMotd extends Command {
    @Inject private RoleUtils roleUtils;

    public SetMotd() {
        super("?setmotd ", "", false);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (roleUtils.isStaff(e.getMember())) {
            if (args.length == 1) {
                e.getChannel().getManager().setTopic(args[0].replace("\"", "")).queue();
            }
        }
    }
}