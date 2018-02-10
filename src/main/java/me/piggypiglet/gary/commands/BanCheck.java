package me.piggypiglet.gary.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.util.RoleUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class BanCheck extends Command {
    @Inject private RoleUtil rutil;

    public BanCheck() {
        cmd = "?bancheck";
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    public void execute(MessageReceivedEvent e, String[] args) {
        if (rutil.isStaff(e.getMember())) {
            if (args.length == 1) {
                if (args[0].length() >= 5) {
                    if (Character.toString(args[0].charAt(args[0].length() - 5)).equalsIgnoreCase("#")) {
                        List<Guild.Ban> bans = e.getGuild().getBanList().complete();
                        Map<String, String> users = new HashMap<>();
                        bans.forEach(ban -> users.put(ban.getUser().getName() + "#" + ban.getUser().getDiscriminator(), ban.getReason()));
                        Set set = users.entrySet();
                        String banned = "";
                        for (Object aSet : set) {
                            Map.Entry entry = (Map.Entry) aSet;
                            if (entry.getKey().toString().equalsIgnoreCase(args[0])) {
                                banned = args[0] + " is banned.";
                                break;
                            } else {
                                banned = args[0] + " is not banned.";
                            }
                        }
                        e.getChannel().sendMessage(banned).queue();
                    } else {
                        e.getChannel().sendMessage("Invalid name/discriminator").queue();
                    }
                } else if (args[0].length() == 0) {
                    e.getChannel().sendMessage("Incorrect usage of `?bancheck`. Use like this: `?bancheck username#discriminator`").queue();
                } else {
                    e.getChannel().sendMessage("Invalid name/discriminator").queue();
                }
            }
        }
    }
}
