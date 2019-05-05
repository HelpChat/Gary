package me.piggypiglet.gary.core.handlers.misc;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.enums.ChatSettings;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.utils.mysql.MySQLUtils;
import me.piggypiglet.gary.core.utils.mysql.SettingsUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.managers.GuildController;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ChatSettingsHandler extends GEvent {
    public ChatSettingsHandler() {
        super(EventsEnum.MESSAGE_REACTION_ADD, EventsEnum.MESSAGE_REACTION_REMOVE);
    }

    @Override
    protected void execute(GenericEvent event) {
        GenericGuildMessageReactionEvent e = (GenericGuildMessageReactionEvent) event;
        MessageReaction.ReactionEmote emote = e.getReactionEmote();
        Guild guild = e.getGuild();

        if (e.getMessageIdLong() == 574493964724469780L && !e.getUser().isBot()) {
            ChatSettings setting = ChatSettings.getSetting(emote);
            GuildController controller = guild.getController();

            Member member = e.getMember();
            Role role = guild.getRoleById(setting.roleID);
            long id = member.getIdLong();

            switch (EventsEnum.fromEvent(event)) {
                case MESSAGE_REACTION_ADD:
                    controller.addRolesToMember(member, role).queue();
                    set(setting, emote, id, true);
                    break;

                case MESSAGE_REACTION_REMOVE:
                    controller.removeRolesFromMember(member, role).queue();
                    set(setting, emote, id, false);
                    break;
            }
        }
    }

    private void set(ChatSettings setting, MessageReaction.ReactionEmote emote, long userID, boolean value) {
        if (!MySQLUtils.exists("gary_settings", new String[]{"user_id"}, new Object[]{userID})) {
            SettingsUtils.add(userID, setting);
        } else {
            SettingsUtils.set(ChatSettings.getSetting(emote), userID, value ? 1 : 0);
        }
    }
}
