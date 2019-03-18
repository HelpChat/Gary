package me.piggypiglet.gary.core.handlers.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.objects.selfexpiringmap.SelfExpiringHashMap;
import me.piggypiglet.gary.core.objects.selfexpiringmap.SelfExpiringMap;
import me.piggypiglet.gary.core.storage.file.GFile;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class RaidHandler extends GEvent {
    @Inject private GFile gFile;

    private final SelfExpiringMap<String, Member> expiringMap = new SelfExpiringHashMap<>();

    public RaidHandler() {
        super(EventsEnum.MESSAGE_CREATE);
    }

    @Override
    protected void execute(GenericEvent event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;
        Member member = e.getMember();

        if (member.getRoles().isEmpty() && !e.getAuthor().isBot()) {
            String message = e.getMessage().getContentRaw();
            List<String> raidWords = gFile.getFileConfiguration("config").getStringList("raid-words");
            
            if (StringUtils.contains(message, raidWords)) {
                EmbedBuilder builder = new EmbedBuilder()
                        .addField("⚠️AntiRaid Filter Trigger!",
                                "some goofy dude called " + e.getAuthor().getAsMention() + " gon be triggering the raid alerts in " + e.getChannel().getAsMention() + " smh",
                                false)
                        .addField("Message:", "```" + message + "```", false)
                        .setTimestamp(ZonedDateTime.now());
                Guild guild = e.getGuild();

                if (expiringMap.containsKey(message)) {
                    e.getMessage().delete().queue();
                    guild.getController().addRolesToMember(member, guild.getRoleById(Constants.GLOBAL_MUTE)).queue();
                    builder.setFooter("I've muted them to prevent further spamming.", e.getJDA().getSelfUser().getEffectiveAvatarUrl());
                }

                expiringMap.put(message, member, TimeUnit.MINUTES.toMillis(1));
                guild.getTextChannelById(Constants.STAFF).sendMessage(builder.build()).queue();
            }
        }
    }
}
