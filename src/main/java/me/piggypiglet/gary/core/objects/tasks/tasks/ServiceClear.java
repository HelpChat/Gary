package me.piggypiglet.gary.core.objects.tasks.tasks;

import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.tasks.GRunnable;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.mysql.BumpUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Stream;

import static net.dv8tion.jda.api.Permission.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServiceClear extends GRunnable {
    @Inject private GaryBot garyBot;

    @Override
    @SuppressWarnings("ConstantConditions")
    public void run() {
        if (ZonedDateTime.now().getDayOfMonth() == 1) {
            Guild guild = garyBot.getJda().getGuildById(Constants.GUILD);

            Member gary = guild.getMemberById(332142935380459520L);
            Member barry = guild.getMemberById(532800069565546496L);
            List<Permission> bots = Arrays.asList(MANAGE_PERMISSIONS, MESSAGE_READ, MESSAGE_WRITE, MESSAGE_MANAGE, MESSAGE_EMBED_LINKS, MANAGE_CHANNEL);

            Role everyone = guild.getPublicRole();
            List<Permission> everyonePerms = Arrays.asList(MESSAGE_READ, MESSAGE_WRITE);

            Role sm = guild.getRoleById(452659172534648843L);
            Role gm = guild.getRoleById(487901903737454592L);
            List<Permission> mutePerms = Collections.singletonList(MESSAGE_READ);

            Role admin = guild.getRoleById(164525396354793472L);
            List<Permission> adminPerms = Arrays.asList(MESSAGE_READ, MESSAGE_WRITE, MANAGE_PERMISSIONS);

            Stream.of("offer-services", "rate-my-server", "request-free", "request-paid").forEach(s -> {
                guild.getTextChannelsByName(s, false).get(0).delete().queue();
                guild.getController().createTextChannel(s)
                        .addPermissionOverride(gary, bots, new ArrayList<>())
                        .addPermissionOverride(barry, bots, new ArrayList<>())
                        .addPermissionOverride(everyone, new ArrayList<>(), everyonePerms)
                        .addPermissionOverride(sm, new ArrayList<>(), mutePerms)
                        .addPermissionOverride(gm, new ArrayList<>(), mutePerms)
                        .addPermissionOverride(admin, adminPerms, new ArrayList<>())
                        .queue(c -> {
                            String prefix = "formats." + s + ".";

                            c.sendMessage(Lang.getALString( prefix + "channel-message",
                                    Lang.getALString(prefix + "requirements"),
                                    Lang.getALString(prefix + "template"),
                                    ZonedDateTime.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH))).queue(m -> m.pin().queue(p -> {
                                        c.getHistoryAfter(m.getId(), 1).queue(mh -> mh.getRetrievedHistory().get(0).delete().queue());
                            }));

                            c.getManager().setParent(c.getGuild().getCategoryById(Constants.SERVICES)).queue();
                        });
            });

            BumpUtils.clear();
        }
    }
}
