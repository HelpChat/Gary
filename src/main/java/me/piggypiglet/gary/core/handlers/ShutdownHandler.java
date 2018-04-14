package me.piggypiglet.gary.core.handlers;

import com.google.inject.Inject;
import me.piggypiglet.gary.ChatReaction;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class ShutdownHandler extends Thread {
    @Inject private ChatReaction chatReaction;

    @Override
    public void run() {
        chatReaction.getCurrentMessage().delete().queue();
    }
}
