package me.piggypiglet.gary.commands.misc;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.storage.json.GTypes;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// ------------------------------
// Copyright (c) PiggyPiglet ${year}
// https://www.piggypiglet.me
// ------------------------------
public final class AI extends Command {
    @Inject private MessageUtils mutil;
    @Inject private GTypes gTypes;

    private Logger logger;

    public AI() {
        super("!/!say ", "Speak to gary, or speak through gary.", true);
        this.delete = false;
        logger = LoggerFactory.getLogger("AI");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        MessageChannel channel = e.getChannel();

        if (channel.getIdLong() == 339674158596358145L) {
            AIConfiguration config = new AIConfiguration(gTypes.getString("config", "aitoken"));
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
                    logger.error(response.getStatus().getErrorDetails());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
