package me.piggypiglet.gary.core.objects.pagination;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.MessageEmbed;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PaginationPage {
    private Object message;
    private Object emote;

    public PaginationPage set(String message, Emote emote) {
        this.message = message;
        this.emote = emote;

        return this;
    }

    public PaginationPage set(MessageEmbed message, Emote emote) {
        this.message = message;
        this.emote = emote;

        return this;
    }

    public PaginationPage set(String message, String emote) {
        this.message = message;
        this.emote = emote;

        return this;
    }

    public PaginationPage set(MessageEmbed message, String emote) {
        this.message = message;
        this.emote = emote;

        return this;
    }

    public Object getMessage() {
        return message;
    }

    public Object getEmote() {
        return emote;
    }
}
