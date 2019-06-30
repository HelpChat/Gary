package me.piggypiglet.gary.permission;

import me.piggypiglet.gary.file.implementations.json.types.Roles;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class PermissionMember {
    private final Member member;

    public PermissionMember(Member member) {
        this.member = member;
    }

    public boolean hasPermission(String permission) {
        return member.getRoles().stream().map(Role::getId).anyMatch(i -> Roles.hasPermission(i, permission));
    }
}