package me.piggypiglet.gary.conversation;

import com.google.inject.Singleton;
import me.piggypiglet.gary.tasks.Task;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class ConversationEventListener extends ListenerAdapter {
    private final List<Conversation> conversations = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();

        assert e.getMember() != null;

        for (Conversation conversation : conversations) {
            if (conversation.match(e.getMember(), e.getChannel())) {
                try {
                    Prompt current = conversation.getPrompts().get(0);

                    if (current.getMatch().test(message)) {
                        conversation.getCompletedAnswers().put(current.getKey(), message);

                        if (current.isEnd()) {
                            conversation.getAnswers().complete(conversation.getCompletedAnswers());
                            Task.async(r -> conversations.remove(conversation));
                        } else {
                            conversation.getPrompts().remove(0);
                            e.getChannel().sendMessage(conversation.getPrompts().get(0).getQuestion()).queue();
                        }
                    } else {
                        e.getChannel().sendMessage("doesn't match requirements").queue();
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    public Map<String, String> converse(Conversation conversation) {
        conversation.getTextChannel().sendMessage(conversation.getPrompts().get(0).getQuestion()).queue();
        conversations.add(conversation);

        Task.async(r -> conversations.remove(conversation), "5 mins");

        while (!conversation.getAnswers().isDone() && conversations.contains(conversation)) {}

        try {
            return conversation.getAnswers().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }
}
