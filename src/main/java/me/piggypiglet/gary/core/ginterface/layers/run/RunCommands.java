package me.piggypiglet.gary.core.ginterface.layers.run;

import com.google.inject.Singleton;
import lombok.Getter;
import me.piggypiglet.gary.core.ginterface.Top;
import me.piggypiglet.gary.core.objects.enums.ginterface.TopEnum;
import me.piggypiglet.gary.core.objects.enums.ginterface.types.RunType;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class RunCommands extends Top {
    @Getter private List<RunAbstract> runTypes;

    public RunCommands() {
        super(TopEnum.RUN_EXECUTE);
        runTypes = new ArrayList<>();
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentStripped();

        for (RunType value : RunType.values()) {
            String strValue = value.toString().toLowerCase();

            if (StringUtils.contains(message, strValue)) {
                executeRun(value, e);
            }
        }
    }

    private void executeRun(RunType type, GuildMessageReceivedEvent e) {
        for (RunAbstract runAbstract : runTypes) {
            if (runAbstract.getType() == type) {
                runAbstract.run(e);
            }
        }
    }
}