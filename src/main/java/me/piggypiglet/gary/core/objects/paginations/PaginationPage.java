package me.piggypiglet.gary.core.objects.paginations;

import lombok.Getter;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.MessageEmbed;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PaginationPage {
    @Getter private Object message;
    @Getter private Object emote;

    public PaginationPage(String message, Emote emote) {
        this.message = message;
        this.emote = emote;
    }

    public PaginationPage(MessageEmbed message, Emote emote) {
        this.message = message;
        this.emote = emote;
    }

    public PaginationPage(String message, String emote) {
        this.message = message;
        this.emote = emote;
    }

    public PaginationPage(MessageEmbed message, String emote) {
        this.message = message;
        this.emote = emote;
    }
}