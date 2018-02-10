package me.piggypiglet.gary.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.util.MessageUtil;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class AI extends Command {

    @Inject private MessageUtil mutil;

    public AI() {
        super("!");

        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        MessageChannel channel = e.getChannel();

        if (channel.getIdLong() == 339674158596358145L) {
            AIConfiguration config = new AIConfiguration(Constants.AI_TOKEN);
            AIDataService data = new AIDataService(config);

            try {
                AIRequest request = new AIRequest(e.getMessage().getContentRaw().replace("!", ""));
                request.setSessionId(e.getAuthor().getId());
                AIResponse response = data.request(request);

                if (response.getStatus().getCode() == 200) {
                    channel.sendMessage(mutil.format(e, response.getResult().getFulfillment().getSpeech())).queue();
                } else {
                    System.out.println(response.getStatus().getErrorDetails());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
