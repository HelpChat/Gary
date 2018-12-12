package me.piggypiglet.gary.core.objects.enums;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public enum ChatSettings {
    GLOBAL_ANNOUNCEMENTS(498452848787324928L, 498454721275756545L),
    PLUGIN_UPDATES(498453814136012815L, 498454945910095877L),
    PAPI_GIT(498454341955616769L, 498455002243923978L),
    CLIP_PING(498454528635568138L, 498455190140354591L),
    CHAT_REACTION(498454582112813074L, 498456348195946512L),
    UNKNOWN(0L, 0L);

    private final long messageID;
    public final long roleID;

    ChatSettings(long messageID, long roleID) {
        this.messageID = messageID;
        this.roleID = roleID;
    }

    public static ChatSettings getSetting(long messageID) {
        for (ChatSettings setting : values()) {
            if (setting.messageID == messageID) {
                return setting;
            }
        }

        return UNKNOWN;
    }
}
