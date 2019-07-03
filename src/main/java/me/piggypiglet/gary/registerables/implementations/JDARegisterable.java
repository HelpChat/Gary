package me.piggypiglet.gary.registerables.implementations;

import com.google.inject.Inject;
import me.piggypiglet.gary.conversation.ConversationEventListener;
import me.piggypiglet.gary.file.framework.FileConfiguration;
import me.piggypiglet.gary.guice.annotations.Config;
import me.piggypiglet.gary.registerables.Registerable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class JDARegisterable extends Registerable {
    @Inject @Config private FileConfiguration config;
    @Inject private ConversationEventListener conversationEventListener;

    @Override
    protected void execute() {
        try {
            addBinding(JDA.class, new JDABuilder(config.getString("token"))
                    .setActivity(Activity.playing("testing"))
                    .addEventListeners(conversationEventListener)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}