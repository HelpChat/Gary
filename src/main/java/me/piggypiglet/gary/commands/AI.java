package me.piggypiglet.gary.commands;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public class AI extends Command {
    @Inject
    private MessageUtil mutil;

    public AI() {
        cmd = "!";
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    public void execute(MessageReceivedEvent e, String[] args) {
        if (e.getChannel().getId().equalsIgnoreCase("339674158596358145")) {
            AIConfiguration config = new AIConfiguration("");
            AIDataService data = new AIDataService(config);
            try {
                AIRequest request = new AIRequest(e.getMessage().getContentRaw().replace("!", ""));
                request.setSessionId(e.getAuthor().getId());
                AIResponse response = data.request(request);
                if (response.getStatus().getCode() == 200) {
                    e.getChannel().sendMessage(mutil.format(e, response.getResult().getFulfillment().getSpeech())).queue();
                } else {
                    System.out.println(response.getStatus().getErrorDetails());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
