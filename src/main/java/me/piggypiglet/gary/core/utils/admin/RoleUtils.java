package me.piggypiglet.gary.core.utils.admin;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.Objects;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RoleUtils {

    public Role getRole(Guild guild, String name) {
        return guild.getRolesByName(name, true).stream().filter(Objects::nonNull).findFirst().orElse(null);
    }

    public boolean isStaff(Member member) {
// TODO:       Role staff = member.getGuild().getRoleById("411094569850109955"); Why doesn't this work?
        Guild guild = member.getGuild();

        return member.getRoles().stream().anyMatch(role -> {
            Role admin = getRole(guild, "admins"), trusted = getRole(guild, "trusted"), helpful = getRole(guild, "helpful");
            return role.equals(admin) || role.equals(trusted) || role.equals(helpful);
        });
    }

    public boolean isTrustedPlus(Member member) {
        Guild guild = member.getGuild();

        return member.getRoles().stream().anyMatch(role -> {
            Role admin = getRole(guild, "admins"), trusted = getRole(guild, "trusted");
            return role.equals(admin) || role.equals(trusted);
        });
    }

}