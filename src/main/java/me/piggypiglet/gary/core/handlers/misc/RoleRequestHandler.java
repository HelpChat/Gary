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
import me.piggypiglet.gary.core.utils.http.HasteUtils;
import me.piggypiglet.gary.core.utils.misc.QuestionnaireUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@SuppressWarnings("ConstantConditions")
public final class RoleRequestHandler extends GEvent {
    @Inject private GaryBot garyBot;

    private final Map<String, String> ids = new HashMap<>();
    private final Map<String, String> reserved = new HashMap<>();
    private final static Logger LOGGER = LoggerFactory.getLogger("RoleRequest");

    public RoleRequestHandler() {
        super(EventsEnum.MESSAGE_CREATE, EventsEnum.MESSAGE_REACTION_ADD);
    }

    @Override
    protected void execute(GenericEvent event) {
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
                                        .setDescription(HasteUtils.haste(values.get("proof")))
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

                if (ids.containsKey(id) && !user.isBot()) {
                    String name = user.getName();
                    LOGGER.info("{} just responded to a request", name);

                    e2.getChannel().retrieveMessageById(id).queue(s -> {
                        MessageEmbed embed = s.getEmbeds().get(0);
                        Member applicant = guild.getMemberById(getTitleId(embed.getAuthor().getName()));
                        RequestableRoles role = RequestableRoles.getFromAlias(embed.getFields().get(0).getValue());
                        String formattedName = user.getName() + "#" + user.getDiscriminator();

                        switch (e2.getReactionEmote().getName()) {
                            case "✅":
                                if (isNotReserver(id, user.getId())) {
                                    e2.getChannel().sendMessage(user.getAsMention() + " this request has already been reserved.").queue();
                                    LOGGER.info("{} couldn't accept request because it was reserved", name);
                                    return;
                                }

                                while (role == RequestableRoles.DEFAULT) {
                                    String question;

                                    try {
                                        LOGGER.info("{} is telling us what role it is", name);
                                        question = QuestionnaireUtils.askQuestion("I'm not sure what role the user has requested, please type it out.", e2.getMember(), e2.getChannel());
                                    } catch (Exception ex) {
                                        LOGGER.error("{} fucked something up or cancelled", name);
                                        e2.getChannel().sendMessage("Cancelled.").queue();
                                        e2.getReaction().removeReaction(user).queue();
                                        return;
                                    }

                                    role = RequestableRoles.getFromAlias(question);
                                    LOGGER.info("{} said the role is {}", name, role.getRoleId());
                                }

                                guild.getController().addRolesToMember(applicant, guild.getRoleById(role.getRoleId())).queue();
                                LOGGER.info("Adding {} to {}", role.getRoleId(), applicant.getEffectiveName());
                                s.editMessage(new EmbedBuilder(embed).setColor(Constants.GREEN).setFooter("Accepted by " + formattedName, null).build()).queue();
                                LOGGER.info("Updating embed with this information: {} {}", HasteUtils.haste(embed.toData().toString()), formattedName);
                                guild.getTextChannelById(role.getChannelId()).sendMessage("Welcome " + applicant.getAsMention()).queue();
                                LOGGER.info("Sending acceptance message for {} for {}", role.getRoleId(), applicant.getEffectiveName());
                                ids.remove(id);
                                LOGGER.info("Removing id: {}", id);
                                break;

                            case "❌":
                                if (isNotReserver(id, user.getId())) {
                                    e2.getChannel().sendMessage(user.getAsMention() + " this request has already been reserved.").queue();
                                    LOGGER.info("{} couldn't accept request because it was reserved", name);
                                    return;
                                }

                                String question;

                                try {
                                    LOGGER.info("{} is telling why they should be denied", name);
                                    question = QuestionnaireUtils.askQuestion("Why should this user be denied?", e2.getMember(), e2.getChannel());
                                } catch (Exception ex) {
                                    LOGGER.error("{} fucked something up or cancelled", name);
                                    e2.getChannel().sendMessage("Cancelled.").queue();
                                    e2.getReaction().removeReaction(user).queue();
                                    return;
                                }

                                LOGGER.info("{} denied {} for {}", name, user.getName(), question);

                                s.editMessage(new EmbedBuilder(embed).setColor(Constants.RED).addField("Deny reason: ", question, false).setFooter("Denied by " + formattedName, null).build()).queue();
                                LOGGER.info("Editing embed with this info: {} {} {}", embed.toData().toString(), question, formattedName);

                                String msg = "Your role request has been denied, please read the reasons below at re-apply when you have improved.\n" + question;

                                MessageUtils.sendMessageHaste(
                                        msg,
                                        applicant.getUser(),
                                        guild.getTextChannelById(Constants.ROLE_REQUEST),
                                        applicant.getAsMention() + "\nDenied, please read haste for more information.",
                                        new TimeAPI("24hours")
                                );
                                LOGGER.info("{} Sending the message", name);
                                ids.remove(id);
                                LOGGER.info("Removing id: {}", id);
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

        return false;
    }

    public void populateMap() {
        System.out.println("test");
        garyBot.getJda().getTextChannelById(Constants.TBD_REQUESTS).getHistory().retrievePast(100).queue(l -> l.forEach(m -> {
            if (m.getAuthor().isBot()) {
                if (m.getEmbeds().size() >= 1 && m.getEmbeds().get(0).getColor().getRGB() == Constants.BLUE.getRGB()) {
                    String messageId = m.getId();
                    String userId = m.getEmbeds().get(0).getFooter().getText().split("-")[0].replace(" ", "").replace("ID:", "");

                    LOGGER.info("Found: {} by {}", messageId, userId);
                    ids.put(messageId, userId);
                }
            }
        }));
    }

    private String getTitleId(String title) {
        String[] titleSegments = title.split("-");
        return titleSegments[titleSegments.length - 1].replace(" ", "");
    }
}
