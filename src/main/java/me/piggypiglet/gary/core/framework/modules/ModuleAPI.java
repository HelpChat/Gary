package me.piggypiglet.gary.core.framework.modules;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class ModuleAPI {
    // TODO: How about some code here? -- https://paste.extendedclip.com/rotepahagi.swift
    private final String name;

    protected ModuleAPI() {
        this("null");
    }

    protected ModuleAPI(String name) {
        this.name = name;
    }

    protected abstract void onEnable();

    public void start() {
        onEnable();
    }
}
