package me.piggypiglet.gary.core.logging;

import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.EventsEnum;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class Logger {
    private final EventsEnum type;
    private Guild guild;
    private Object[] other;

    protected Logger() {
        this(null);
    }

    protected Logger(EventsEnum type) {
        this.type = type;
    }

    protected abstract MessageEmbed send();

    public void log(JDA jda, Guild guild, Object... other) {
        this.guild = guild;
        this.other = other;

        MessageEmbed message = send();
        if (message != null) {
            jda.getTextChannelById(Constants.LOG).sendMessage(message).queue();
        }
    }

    protected Guild getGuild() {
        return guild;
    }

    protected Object[] getOther() {
        return other;
    }

    public EventsEnum getType() {
        return type;
    }
}
