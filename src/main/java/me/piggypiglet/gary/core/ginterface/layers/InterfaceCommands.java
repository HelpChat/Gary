package me.piggypiglet.gary.core.ginterface.layers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.ginterface.layers.add.AddAbstract;
import me.piggypiglet.gary.core.ginterface.layers.add.AddCommands;
import me.piggypiglet.gary.core.ginterface.layers.clear.ClearAbstract;
import me.piggypiglet.gary.core.ginterface.layers.clear.ClearCommands;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class InterfaceCommands {
    @Inject private ClearCommands clearCommands;
    @Inject private AddCommands addCommands;

    @Getter private List<InterfaceAbstract> interfaceAbstractList;

    public InterfaceCommands() {
        interfaceAbstractList = new ArrayList<>();
    }

    public void sort() {
        if (!interfaceAbstractList.isEmpty()) {
            for (InterfaceAbstract interfaceAbstract : interfaceAbstractList) {
                switch (interfaceAbstract.getTopType()) {
                    case CLEAR_REMOVE:
                        clearCommands.getClearTypes().add((ClearAbstract) interfaceAbstract);
                        break;

                    case ADD_MAKE_CREATE:
                        addCommands.getAddTypes().add((AddAbstract) interfaceAbstract);
                        break;
                }
            }


        }
    }
}
