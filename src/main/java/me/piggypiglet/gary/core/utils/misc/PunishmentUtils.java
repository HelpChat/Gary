package me.piggypiglet.gary.core.utils.misc;

import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.Constants;
import net.dv8tion.jda.core.entities.Guild;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PunishmentUtils {
    @Inject private static GaryBot garyBot;

    public static void update(long userId, int strikes) {
        if (strikes >= 3) {
            Guild guild = garyBot.getJda().getGuildById(Constants.GUILD);
            guild.getController().addRolesToMember(guild.getMemberById(userId), guild.getRoleById(Constants.GLOBAL_MUTE)).queue();
        }
    }
}
