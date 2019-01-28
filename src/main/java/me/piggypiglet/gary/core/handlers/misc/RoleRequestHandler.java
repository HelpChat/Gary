package me.piggypiglet.gary.core.handlers.misc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.objects.enums.RequestableRoles;
import me.piggypiglet.gary.core.objects.services.FormatScanner;
import me.piggypiglet.gary.core.utils.discord.MessageUtils;
import me.piggypiglet.gary.core.utils.discord.RoleUtils;
import me.piggypiglet.gary.core.utils.misc.QuestionnaireUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import sh.okx.timeapi.TimeAPI;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class RoleRequestHandler extends GEvent {
    @Inject private GaryBot garyBot;

    private final Map<String, String> ids = new HashMap<>();
    private final Map<String, String> reserved = new HashMap<>();

    public RoleRequestHandler() {
        super(EventsEnum.MESSAGE_CREATE, EventsEnum.MESSAGE_REACTION_ADD);
    }

    @Override
    protected void execute(Event event) {
        switch (EventsEnum.fromEvent(event)) {
            case MESSAGE_CREATE:
                GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;
                TextChannel channel = e.getChannel();

                if (channel.getIdLong() == Constants.ROLE_REQUEST && !e.getAuthor().isBot()) {
                    Message message = e.getMessage();
                    User author = e.getAuthor();
                    FormatScanner scanner = new FormatScanner(message);

                    if (scanner.containsKeys("role", "proof")) {
                        Map<String, String> values = scanner.getValues();

                        message.delete().queue(
                                s -> channel.sendMessage(author.getAsMention() + " Your application is currently being reviewed, I'll get back to you with the final result soon."
                                ).queue(
                                        s2 -> s2.delete().queueAfter(15, TimeUnit.SECONDS)
                                )
                        );

                        e.getGuild().getTextChannelById(Constants.TBD_REQUESTS).sendMessage(
                                new EmbedBuilder()
                                        .setColor(Constants.BLUE)
                                        .setAuthor(author.getName() + "#" + author.getDiscriminator() + " - " + author.getId(), null, author.getEffectiveAvatarUrl())
                                        .addField("Requesting: ", values.get("role"), false)
                                        .setDescription("```" + values.get("proof") + "```")
                                        .setFooter("ID: " + message.getId() + " - React with ✅ to approve - React with ❌ to deny", null)
                                        .setTimestamp(ZonedDateTime.now())
                                        .build()
                        ).queue(s -> {
                            Stream.of("✅", "❌", "➖").forEach(em -> s.addReaction(em).queue());
                            ids.put(s.getId(), message.getId());
                        });
                    } else {
                        message.delete().queue(s -> channel.sendMessage(author.getAsMention() + " please follow the format pinned in this channel.").queue(s2 -> s2.delete().queueAfter(15, TimeUnit.SECONDS)));
                    }
                }
                break;

            case MESSAGE_REACTION_ADD:
                GuildMessageReactionAddEvent e2 = (GuildMessageReactionAddEvent) event;
                String id = e2.getMessageId();
                User user = e2.getUser();
                Guild guild = e2.getGuild();

                if (ids.containsKey(id) && !user.isBot() && RoleUtils.isTrusted(e2.getMember())) {
                    e2.getChannel().getMessageById(id).queue(s -> {
                        MessageEmbed embed = s.getEmbeds().get(0);
                        Member applicant = guild.getMemberById(embed.getAuthor().getName().split("-")[1].replace(" ", ""));
                        RequestableRoles role = RequestableRoles.getFromAlias(embed.getFields().get(0).getValue());
                        String formattedName = user.getName() + "#" + user.getDiscriminator();

                        switch (e2.getReactionEmote().getName()) {
                            case "✅":
                                if (isNotReserver(id, user.getId())) {
                                    e2.getChannel().sendMessage(user.getAsMention() + " this request has already been reserved.").queue();
                                    return;
                                }

                                while (role == RequestableRoles.DEFAULT) {
                                    String question;

                                    try {
                                        question = QuestionnaireUtils.askQuestion("I'm not sure what role the user has requested, please type it out.", e2.getMember(), e2.getChannel());
                                    } catch (Exception ex) {
                                        e2.getChannel().sendMessage("Cancelled.").queue();
                                        e2.getReaction().removeReaction(user).queue();
                                        return;
                                    }

                                    role = RequestableRoles.getFromAlias(question);
                                }

                                guild.getController().addRolesToMember(applicant, guild.getRoleById(role.getRoleId())).queue();
                                s.editMessage(new EmbedBuilder(embed).setColor(Constants.GREEN).setFooter("Accepted by " + formattedName, null).build()).queue();
                                guild.getTextChannelById(role.getChannelId()).sendMessage("Welcome " + applicant.getAsMention()).queue();
                                ids.remove(id);
                                break;

                            case "❌":
                                if (isNotReserver(id, user.getId())) {
                                    e2.getChannel().sendMessage(user.getAsMention() + " this request has already been reserved.").queue();
                                    return;
                                }

                                String question;

                                try {
                                    question = QuestionnaireUtils.askQuestion("Why should this user be denied?", e2.getMember(), e2.getChannel());
                                } catch (Exception ex) {
                                    e2.getChannel().sendMessage("Cancelled.").queue();
                                    e2.getReaction().removeReaction(user).queue();
                                    return;
                                }

                                s.editMessage(new EmbedBuilder(embed).setColor(Constants.RED).addField("Deny reason: ", question, false).setFooter("Denied by " + formattedName, null).build()).queue();

                                String msg = "Your role request has been denied, please read the reasons below at re-apply when you have improved.\n" + question;

                                MessageUtils.sendMessageHaste(
                                        msg,
                                        applicant.getUser(),
                                        guild.getTextChannelById(Constants.ROLE_REQUEST),
                                        applicant.getAsMention() + "\nDenied, please read haste for more information.",
                                        new TimeAPI("24hours")
                                );
                                ids.remove(id);
                                break;

                            case "➖":
                                s.editMessage(new EmbedBuilder(embed).setColor(Constants.YELLOW).setFooter(embed.getFooter().getText(), user.getEffectiveAvatarUrl()).build()).queue();
                                reserved.put(id, user.getId());
                                e2.getChannel().sendMessage("I've reserved this request for you.").queue(s1 -> s1.delete().queueAfter(10, TimeUnit.SECONDS));
                                break;
                        }
                    });
                }
                break;
        }
    }

    private boolean isNotReserver(String messageId, String authorId) {
        if (reserved.containsKey(messageId)) {
            return !reserved.get(messageId).equals(authorId);
        }

        return true;
    }

    public void populateMap() {
        garyBot.getJda().getTextChannelById(Constants.TBD_REQUESTS).getHistory().retrievePast(100).queue(l -> l.forEach(m -> {
            if (m.getAuthor().isBot()) {
                if (m.getEmbeds().size() >= 1 && m.getEmbeds().get(0).getColor().getRGB() == Constants.BLUE.getRGB()) {
                    ids.put(m.getId(), m.getEmbeds().get(0).getFooter().getText().split("-")[0].replace(" ", "").replace("ID:", ""));
                }
            }
        }));
    }
}
