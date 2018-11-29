package me.piggypiglet.gary.core.handlers.chat;

import me.piggypiglet.gary.core.handlers.GEvent;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.events.Event;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by GlareMasters
 * Date: 10/31/2018
 * Time: 2:17 AM
 */
public final class SpamHandler extends GEvent {
    private Set<String> messages = new HashSet<>();

    public SpamHandler() {
        super(EventsEnum.MESSAGE_CREATE);
    }

    @Override
    protected void execute(Event event) {
//        GenericGuildMessageEvent e = (GenericGuildMessageEvent) event;
//        Message message = e.getChannel().getMessageById(e.getMessageId()).complete();
//        if (message.getAuthor().isBot()) return;
//        if (messages.contains(message.getContentRaw())) {
//            message.delete().queue();
//        } else {
//            messages.add(message.getContentRaw());
//
//        }
    }

}
