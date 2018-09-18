package me.piggypiglet.gary.core.ginterface.layers;

import lombok.Getter;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class InterfaceAbstract {
    @Getter private final TopEnum topType;

    protected InterfaceAbstract() {
        this(null);
    }

    protected InterfaceAbstract(TopEnum topType) {
        this.topType = topType;
    }
}
