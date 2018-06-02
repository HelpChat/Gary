package me.piggypiglet.gary.core.ai;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.ai.layers.clear.ClearAbstract;
import me.piggypiglet.gary.core.ai.layers.clear.ClearCommands;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class InterfaceCommands {
    @Inject private ClearCommands clearCommands;

    private List<InterfaceAbstract> interfaceAbstractList;

    public InterfaceCommands() {
        interfaceAbstractList = new ArrayList<>();
    }

    public List<InterfaceAbstract> getInterfaceAbstractList() {
        return interfaceAbstractList;
    }

    public void sort() {
        if (!interfaceAbstractList.isEmpty()) {
            for (InterfaceAbstract interfaceAbstract : interfaceAbstractList) {
                switch (interfaceAbstract.getTopType()) {
                    case CLEAR:
                        clearCommands.getClearTypes().add((ClearAbstract) interfaceAbstract);
                        break;
                }
            }
        }
    }
}
