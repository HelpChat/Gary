package me.piggypiglet.gary.commands.implementations.admin;

import com.google.inject.Inject;
import me.piggypiglet.gary.commands.Command;
import me.piggypiglet.gary.conversation.Conversation;
import me.piggypiglet.gary.conversation.ConversationHandler;
import me.piggypiglet.gary.conversation.Prompt;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class TestCommand extends Command {
    @Inject
    private ConversationHandler conversationHandler;

    public TestCommand() {
        super("test");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        Map<String, String> conversation = conversationHandler.converse(
                Conversation.builder().member(e.getMember()).textChannel(e.getChannel()).prompts(Collections.singletonList(
                        Prompt.builder().question("say a number.").key("test").match(s -> {
                            try {
                                Integer.parseInt(s);
                                return true;
                            } catch (Exception ex) {
                                return false;
                            }
                        }).isEnd(true).build()
                )).build()
        );

        e.getChannel().sendMessage(conversation.get("test")).queue();
    }
}