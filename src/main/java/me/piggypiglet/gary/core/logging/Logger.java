package me.piggypiglet.gary.core.logging;

import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.LogType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class Logger {
    private final LogType type;
    private User user;
    private Channel channel;
    private Message message;
    private Long messageId;
    private Guild guild;

    protected Logger() {
        this(null);
    }

    protected Logger(LogType type) {
        this.type = type;
    }

    protected abstract MessageEmbed send();

    public void log(JDA jda, User user, Channel channel, Message message, Long messageId, Guild guild) {
        this.user = user;
        this.channel = channel;
        this.message = message;
        this.messageId = messageId;
        this.guild = guild;

        jda.getTextChannelById(Constants.LOG).sendMessage(send()).queue();
    }

    protected User getUser() {
        return user;
    }

    protected Channel getChannel() {
        return channel;
    }

    protected Message getMessage() {
        return message;
    }

    protected long getMessageId() {
        return messageId;
    }

    protected Guild getGuild() {
        return guild;
    }

    public LogType getType() {
        return type;
    }
}
