package me.piggypiglet.gary.commands;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Config;
import me.piggypiglet.gary.core.utils.channel.MessageUtils;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class AI extends Command {

    @Inject private MessageUtils mutil;
    @Inject private Config config;

    public AI() {
        super("!/!say ");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        MessageChannel channel = e.getChannel();

        if (channel.getIdLong() == 339674158596358145L) {
            AIConfiguration config = new AIConfiguration(this.config.getItem("aitoken"));
            AIDataService data = new AIDataService(config);

            try {
                AIRequest request = new AIRequest(e.getMessage().getContentRaw().replace("!", ""));
                request.setSessionId(e.getAuthor().getId());
                AIResponse response = data.request(request);

                if (response.getStatus().getCode() == 200) {
                    if (e.getMessage().getContentRaw().startsWith("!say ")) {
                        channel.sendMessage(mutil.format(e, e.getMessage().getContentRaw()
                        .replace("!say ", ""))).queue();
                    } else {
                        channel.sendMessage(mutil.format(e, response.getResult().getFulfillment().getSpeech())).queue();
                    }
                } else {
                    System.out.println(response.getStatus().getErrorDetails());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
