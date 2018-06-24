package me.piggypiglet.gary.core.ginterface.layers.add.types;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.ginterface.layers.add.AddAbstract;
import me.piggypiglet.gary.core.objects.enums.ginterface.clear.AddType;
import me.piggypiglet.gary.core.utils.web.WebUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Reaction extends AddAbstract {
    @Inject private WebUtils webUtils;

    public Reaction() {
        super(AddType.REACTION);
    }

    @Override
    protected void execute(MessageReceivedEvent e) {
        String content = e.getMessage().getContentDisplay();
        TextChannel channel = e.getTextChannel();

        Pattern pattern = Pattern.compile("([0-9])");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            Message message;
            long id = Stream.of(content.replaceAll("^\\D+", "").split("\\D+"))
                    .mapToLong(Long::parseLong)
                    .max().orElse(-1);

            try {
                message = channel.getMessageById(id).complete();
            } catch (Exception ex) {
                channel.sendMessage("The id you specified is invalid.").queue();
                return;
            }

            channel.sendMessage(webUtils.hastebin(content)).queue();
        } else {
            channel.sendMessage("You didn't include an id in your message.").queue();
        }
    }
}
