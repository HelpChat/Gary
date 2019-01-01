package me.piggypiglet.gary.core.objects.tasks.tasks;

import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.tasks.GRunnable;
import me.piggypiglet.gary.core.storage.file.Lang;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static net.dv8tion.jda.core.Permission.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServiceClear extends GRunnable {
    @Inject private GaryBot garyBot;

    @Override
    public void run() {
        if (ZonedDateTime.now().getDayOfMonth() == 1) {
            Guild guild = garyBot.getJda().getGuildById(Constants.GUILD);

            Member gary = guild.getMemberById(332142935380459520L);
            List<Permission> garyPerms = new ArrayList<>();
            Stream.of(MANAGE_PERMISSIONS, MESSAGE_READ, MESSAGE_WRITE, MESSAGE_MANAGE, MESSAGE_EMBED_LINKS, MANAGE_CHANNEL).forEach(garyPerms::add);

            Role everyone = guild.getPublicRole();
            List<Permission> everyonePerms = new ArrayList<>();
            Stream.of(MESSAGE_READ, MESSAGE_WRITE).forEach(everyonePerms::add);

            Role sm = guild.getRoleById(452659172534648843L);
            List<Permission> smPerms = new ArrayList<>();
            Stream.of(MESSAGE_READ).forEach(smPerms::add);

            Role gm = guild.getRoleById(487901903737454592L);
            List<Permission> gmPerms = new ArrayList<>();
            Stream.of(MESSAGE_READ).forEach(gmPerms::add);

            Role admin = guild.getRoleById(164525396354793472L);
            List<Permission> adminPerms = new ArrayList<>();
            Stream.of(MESSAGE_READ, MESSAGE_WRITE, MANAGE_PERMISSIONS).forEach(adminPerms::add);

            Stream.of("offer-services", "rate-my-server", "request-free", "request-paid").forEach(s -> {
                guild.getTextChannelsByName(s, false).get(0).delete().queue();
                guild.getController().createTextChannel(s)
                        .addPermissionOverride(gary, garyPerms, new ArrayList<>())
                        .addPermissionOverride(everyone, new ArrayList<>(), everyonePerms)
                        .addPermissionOverride(sm, new ArrayList<>(), smPerms)
                        .addPermissionOverride(gm, new ArrayList<>(), gmPerms)
                        .addPermissionOverride(admin, adminPerms, new ArrayList<>())
                        .queue(c -> {
                            ((TextChannel) c).sendMessage(String.join("\n", Lang.getAlternateList("formats." + s + ".channel-message",
                                    String.join("\n", Lang.getAlternateList("formats." + s + ".requirements")),
                                    String.join("\n", Lang.getAlternateList("formats." + s + ".template")),
                                    ZonedDateTime.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)))).queue();
                            c.getManager().setParent(c.getGuild().getCategoryById(Constants.SERVICES)).queue();
                        });
            });
        }
    }
}
