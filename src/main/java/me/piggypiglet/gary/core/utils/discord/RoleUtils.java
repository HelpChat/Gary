package me.piggypiglet.gary.core.utils.discord;

import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.function.Predicate;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RoleUtils {
    public static boolean isHelpful(Member member) {
        return containsRoles(member, r -> r == Constants.HELPFUL || isTrusted(member) || isAdmin(member));
    }

    public static boolean isTrusted(Member member) {
        return containsRoles(member, r -> r == Constants.TRUSTED || isAdmin(member));
    }

    public static boolean isAdmin(Member member) {
        return containsRoles(member, r -> r == Constants.ADMIN);
    }

    private static boolean containsRoles(Member member, Predicate<? super Long> predicate) {
        return member.getRoles().stream().map(Role::getIdLong).anyMatch(predicate);
    }
}
