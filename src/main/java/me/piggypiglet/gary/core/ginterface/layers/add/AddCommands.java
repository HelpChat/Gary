package me.piggypiglet.gary.core.ginterface.layers.add;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.piggypiglet.gary.core.ginterface.Top;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ginterface.clear.AddType;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class AddCommands extends Top {
    @Inject private MessageUtils messageUtils;

    private List<AddAbstract> addTypes;

    public AddCommands() {
        super(TopEnum.ADD);
        addTypes = new ArrayList<>();
    }

    public List<AddAbstract> getAddTypes() {
        return addTypes;
    }

    @Override
    protected void execute(MessageReceivedEvent e) {
        String message = e.getMessage().getContentStripped();

        for (AddType value : AddType.values()) {
            String strValue = value.toString().toLowerCase();

            if (messageUtils.contains(message, strValue)) {
                executeAdd(value, e);
            }
        }
    }

    private void executeAdd(AddType type, MessageReceivedEvent e) {
        for (AddAbstract addAbstract : addTypes) {
            if (addAbstract.getType() == type) {
                addAbstract.run(e);
            }
        }
    }
}
