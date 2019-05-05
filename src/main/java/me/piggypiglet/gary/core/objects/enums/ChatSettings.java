package me.piggypiglet.gary.core.objects.enums;

import net.dv8tion.jda.api.entities.MessageReaction;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public enum ChatSettings {
    PLUGIN_UPDATES(498454945910095877L, "Spigot"),
    ECLOUD_UPDATES(574470889459154944L, "eCloud"),
    PAPI_GIT(498455002243923978L, "PAPI"),
    CLIP_PING(498455190140354591L, "Clip"),
    UNKNOWN(0L, "");

    public final long roleID;
    private final String emote;

    ChatSettings(long roleID, String emote) {
        this.roleID = roleID;
        this.emote = emote;
    }

    public static ChatSettings getSetting(MessageReaction.ReactionEmote emote) {
        for (ChatSettings setting : values()) {
            if (emote.getName().equalsIgnoreCase(setting.emote)) {
                return setting;
            }
        }

        return UNKNOWN;
    }
}
