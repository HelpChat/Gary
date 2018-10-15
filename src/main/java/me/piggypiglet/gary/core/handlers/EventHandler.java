package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.handlers.chat.*;
import me.piggypiglet.gary.core.handlers.misc.GiveawayHandler;
import me.piggypiglet.gary.core.handlers.misc.InsultHandler;
import me.piggypiglet.gary.core.handlers.misc.LoggingHandler;
import me.piggypiglet.gary.core.handlers.misc.PaginationHandler;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import me.piggypiglet.gary.core.storage.mysql.tables.Messages;
import me.piggypiglet.gary.core.storage.mysql.tables.Users;
import me.piggypiglet.gary.core.utils.message.StringUtils;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.EventListener;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class EventHandler implements EventListener {
    @Inject private LoggingHandler loggingHandler;
    @Inject private CommandHandler commandHandler;
    @Inject private ShowcaseHandler showcaseHandler;
    @Inject private StatHandler statHandler;
    @Inject private ServiceHandler serviceHandler;
    @Inject private PaginationHandler paginationHandler;
    @Inject private InterfaceHandler interfaceHandler;
    @Inject private GiveawayHandler giveawayHandler;
    @Inject private InsultHandler insultHandler;

    @Inject private Users users;
    @Inject private Messages messages;

    @Override
    public void onEvent(Event event) {

        if (event instanceof GuildMessageReceivedEvent) {
            if (((GuildMessageReceivedEvent) event).getAuthor().isBot()) return;
            GuildMessageReceivedEvent ev = (GuildMessageReceivedEvent) event;
            if (ev.getChannel().getIdLong() != Constants.PLUGIN) return;
            String msg = ev.getMessage().getContentRaw();
            String channel = ev.getChannel().getName();
            if (msg.contains("<@") && msg.contains(">")) {
                ev.getGuild().getTextChannelById(Constants.LOG).sendMessage(ev.getAuthor().getName() + " just tagged someone in " + channel + " and said:\n ```" + msg + "```").queue();
            }
        }

        loggingHandler.check(event);

        switch (EventsEnum.fromEvent(event.getClass())) {
            case MEMBER_JOIN:
                GuildMemberJoinEvent e1 = (GuildMemberJoinEvent) event;

                users.addUser(e1.getUser());

                break;

            case MEMBER_LEAVE:
                GuildMemberLeaveEvent e2 = (GuildMemberLeaveEvent) event;

                users.delUser(e2.getUser().getIdLong());

                break;

            case MESSAGE_CREATE:
                GuildMessageReceivedEvent e3 = (GuildMessageReceivedEvent) event;

                if (!e3.getAuthor().isBot()) {
                    if (StringUtils.equalsIgnoreCase(e3.getChannel().getId(), Constants.CHANNELS)) {
                        messages.addMessage(e3.getMessage());
                    }

                    commandHandler.check(e3);
                    showcaseHandler.check(e3);
                    statHandler.check(e3);
                    serviceHandler.check(e3);
                    interfaceHandler.run(e3);
                }

                break;

            case MESSAGE_DELETE:
                GuildMessageDeleteEvent e4 = (GuildMessageDeleteEvent) event;

                paginationHandler.remove(e4);

                break;

            case REACTION_ADD:
                GuildMessageReactionAddEvent e5 = (GuildMessageReactionAddEvent) event;

                if (!e5.getUser().isBot()) {
                    giveawayHandler.add(e5);
                    paginationHandler.add(e5);
                    insultHandler.check(e5);
                }

                break;

            case REACTION_REMOVE:
                GuildMessageReactionRemoveEvent e6 = (GuildMessageReactionRemoveEvent) event;

                if (!e6.getUser().isBot()) {
                    giveawayHandler.remove(e6);
                }
        }
    }

}
