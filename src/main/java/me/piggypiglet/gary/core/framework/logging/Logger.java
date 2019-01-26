package me.piggypiglet.gary.core.framework.logging;

import lombok.Getter;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

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
    protected List<User> users;
    protected List<TextChannel> channels;
    protected List<Message> messages;
    protected List<String> list;
    protected List<Long> longs;
    protected List<String> strings;
    protected Object[] other;

    protected Logger(EventsEnum type) {
        this.type = type;
    }

    protected abstract MessageEmbed send() throws Exception;

    @SuppressWarnings("unchecked")
    public void log(JDA jda, Guild guild, Object... other) {
        this.guild = guild;
        this.other = other;

        users = new ArrayList<>();
        channels = new ArrayList<>();
        messages = new ArrayList<>();
        list = new ArrayList<>();
        longs = new ArrayList<>();
        strings = new ArrayList<>();

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

        try {
            MessageEmbed messageEmbed = send();

            if (messageEmbed != null) {
                jda.getTextChannelById(Constants.LOG).sendMessage(messageEmbed).queue();
            }
        } catch (Exception e) {
            if (!(e instanceof InsufficientPermissionException)) {
                e.printStackTrace();
            }
        }
    }
}
