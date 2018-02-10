package me.piggypiglet.gary.core.util;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class RoleUtil {
    public Role getRole(Guild guild, String name) {
        List<Role> roles = guild.getRolesByName(name, true);
        if (roles.size() == 1) {
            return roles.get(0);
        }
        return null;
    }

    public boolean isStaff(Member member) {
//        Role staff = member.getGuild().getRoleById("411094569850109955"); Why doesn't this work?
        Role admin = getRole(member.getGuild(), "admins");
        Role trusted = getRole(member.getGuild(), "trusted");
        Role helpful = getRole(member.getGuild(), "helpful");
        return member.getRoles().contains(admin) || member.getRoles().contains(trusted) || member.getRoles().contains(helpful);
    }
}
