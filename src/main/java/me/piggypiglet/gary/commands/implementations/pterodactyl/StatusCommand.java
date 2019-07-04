package me.piggypiglet.gary.commands.implementations.pterodactyl;

import com.google.inject.Inject;
import me.piggypiglet.gary.commands.Command;
import me.piggypiglet.gary.conversation.Conversation;
import me.piggypiglet.gary.conversation.ConversationHandler;
import me.piggypiglet.gary.pterodactyl.Pterodactyl;
import me.piggypiglet.gary.pterodactyl.objects.Server;
import me.piggypiglet.gary.utils.ConversationUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class StatusCommand extends Command {
    @Inject private ConversationHandler conversationHandler;
    @Inject private Pterodactyl pterodactyl;

    public StatusCommand() {
        super("ptero status", "pterodactyl status");
        options.setPermission("pterodactyl.status");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            List<Server> servers = pterodactyl.getServers(args[0]);

            if (servers.size() > 0) {
                if (servers.size() > 1) {
                    Map<String, String> answers = conversationHandler.converse(Conversation.builder().textChannel(e.getChannel()).member(e.getMember()).prompts(Collections.singletonList(
                            ConversationUtils.multiChoice(servers.stream().map(Server::getName).collect(Collectors.toList())).isEnd(true).key("multi").build()
                    )).build());

                    try {
                        e.getChannel().sendMessage(servers.get(Integer.parseInt(answers.get("multi")) - 1).getUserServer().getPowerState().getValue()).queue();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    e.getChannel().sendMessage(servers.get(0).getUserServer().getPowerState().getValue()).queue();
                }
            } else {
                e.getChannel().sendMessage("no servers found").queue();
            }
        }
    }
}
