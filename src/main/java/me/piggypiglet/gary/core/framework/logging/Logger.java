package me.piggypiglet.gary.core.framework.logging;

import lombok.Getter;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class Logger {
    @Getter private final EventsEnum type;
    protected Guild guild;
    protected final List<User> users = new ArrayList<>();
    protected final List<TextChannel> channels = new ArrayList<>();
    protected final List<Message> messages = new ArrayList<>();
    protected List<String> list;
    protected final List<Long> longs = new ArrayList<>();
    protected List<String> strings = new ArrayList<>();
    protected Object[] other;

    protected Logger(EventsEnum type) {
        this.type = type;
    }

    protected abstract MessageEmbed send();

    @SuppressWarnings("unchecked")
    public void log(JDA jda, Guild guild, Object... other) {
        this.guild = guild;
        this.other = other;

        Arrays.stream(other).forEach(obj -> {
            switch (obj.getClass().getSimpleName()) {
                case "UserImpl":
                    users.add((User) obj);
                    break;

                case "TextChannelImpl":
                    channels.add((TextChannel) obj);
                    break;

                case "ReceivedMessage":
                    messages.add((Message) obj);
                    break;

                case "List":
                    for (Object object : (List<?>) obj) {
                        if (object instanceof String) {
                            list = (List<String>) obj;
                        }
                    }
                    break;

                case "Long":
                    longs.add((Long) obj);
                    break;

                case "String":
                    strings.add((String) obj);
                    break;
            }
        });

        MessageEmbed messageEmbed = send();

        if (messageEmbed != null) {
            jda.getTextChannelById(Constants.LOG).sendMessage(messageEmbed).queue();
        }
    }
}
