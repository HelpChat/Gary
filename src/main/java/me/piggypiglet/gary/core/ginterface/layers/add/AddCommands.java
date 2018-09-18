package me.piggypiglet.gary.core.ginterface.layers.add;

import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.ginterface.Top;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ginterface.types.AddType;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class AddCommands extends Top {
    @Getter private List<AddAbstract> addTypes;

    public AddCommands() {
        super(TopEnum.ADD);
        addTypes = new ArrayList<>();
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentStripped();

        for (AddType value : AddType.values()) {
            String strValue = value.toString().toLowerCase();

            if (StringUtils.contains(message, strValue)) {
                executeAdd(value, e);
            }
        }
    }

    private void executeAdd(AddType type, GuildMessageReceivedEvent e) {
        for (AddAbstract addAbstract : addTypes) {
            if (addAbstract.getType() == type) {
                addAbstract.run(e);
            }
        }
    }
}