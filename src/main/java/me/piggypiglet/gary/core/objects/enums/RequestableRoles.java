package me.piggypiglet.gary.core.objects.enums;

import lombok.Getter;
import me.piggypiglet.gary.core.objects.Constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public enum RequestableRoles {
    DEVELOPER(Constants.DEVELOPER_ROLE, Constants.DEVELOPER_CHAT, "dev", "developer", "coder", "programmer"),
    BUILDER(Constants.BUILDER_ROLE, Constants.BUILDER_CHAT, "builder", "build"),
    ARTIST(Constants.ARTIST_ROLE, Constants.ARTIST_CHAT, "art", "artist"),
    SERVER_OWNER(Constants.SERVER_OWNER_ROLE, Constants.SERVER_OWNER_CHAT, "owner", "server owner"),
    DEFAULT(0L, 0L, "null");

    @Getter private final long roleId;
    @Getter private final long channelId;
    private final Set<String> aliases = new HashSet<>();

    RequestableRoles(long roleId, long channelId, String... aliases) {
        this.roleId = roleId;
        this.channelId = channelId;
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public static RequestableRoles getFromAlias(String alias) {
        return Arrays.stream(values()).filter(s -> aliasPredicate(s.aliases, alias)).findFirst().orElse(DEFAULT);
    }

    private static boolean aliasPredicate(Set<String> aliases, String userAlias) {
        return aliases.contains(userAlias.toLowerCase());
    }
}
