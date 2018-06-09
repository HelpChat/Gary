package me.piggypiglet.gary.core.ginterface.layers;

import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class InterfaceAbstract {
    private final TopEnum topType;

    protected InterfaceAbstract() {
        this(null);
    }

    protected InterfaceAbstract(TopEnum topType) {
        this.topType = topType;
    }

    TopEnum getTopType() {
        return topType;
    }
}
