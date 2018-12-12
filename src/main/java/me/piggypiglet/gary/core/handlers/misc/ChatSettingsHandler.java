package me.piggypiglet.gary.core.handlers.misc;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.enums.ChatSettings;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.utils.mysql.MySQLUtils;
import me.piggypiglet.gary.core.utils.mysql.SettingsUtils;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ChatSettingsHandler extends GEvent {
    public ChatSettingsHandler() {
        super(EventsEnum.MESSAGE_REACTION_ADD, EventsEnum.MESSAGE_REACTION_REMOVE);
    }

    @Override
    protected void execute(Event event) {
        switch (EventsEnum.fromEvent(event)) {
            case MESSAGE_REACTION_ADD:
                GuildMessageReactionAddEvent e1 = (GuildMessageReactionAddEvent) event;
                ChatSettings setting1 = ChatSettings.getSetting(e1.getMessageIdLong());

                e1.getGuild().getController().addRolesToMember(e1.getMember(), e1.getGuild().getRoleById(setting1.roleID)).queue();
                set(setting1, e1.getMessageIdLong(), e1.getUser().getIdLong(), true);
                break;

            case MESSAGE_REACTION_REMOVE:
                GuildMessageReactionRemoveEvent e2 = (GuildMessageReactionRemoveEvent) event;
                ChatSettings setting2 = ChatSettings.getSetting(e2.getMessageIdLong());

                e2.getGuild().getController().removeRolesFromMember(e2.getMember(), e2.getGuild().getRoleById(setting2.roleID)).queue();
                set(setting2, e2.getMessageIdLong(), e2.getUser().getIdLong(), false);
                break;
        }
    }

    private void set(ChatSettings setting, long messageID, long userID, boolean value) {
        if (!MySQLUtils.exists("gary_settings", "user_id", userID)) {
            SettingsUtils.add(userID, setting);
        } else {
            SettingsUtils.set(ChatSettings.getSetting(messageID), userID, value ? 1 : 0);
        }
    }
}
