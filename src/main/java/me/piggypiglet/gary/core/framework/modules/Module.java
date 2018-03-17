package me.piggypiglet.gary.core.framework.modules;

import net.dv8tion.jda.core.JDA;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class Module {
    // TODO: How about some code here? -- https://paste.extendedclip.com/rotepahagi.swift
    private final String name;

    protected Module() {
        this("null");
    }

    protected Module(String name) {
        this.name = name;
    }

    protected abstract void onEnable(JDA jda);

    public void start(JDA jda) {
        onEnable(jda);
    }
}
