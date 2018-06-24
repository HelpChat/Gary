package me.piggypiglet.gary.commands.admin;

import co.aikar.idb.DB;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.message.MessageUtils;
import me.piggypiglet.gary.core.utils.web.WebUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.time.ZonedDateTime;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class Eval extends Command {
    @Inject private ScriptEngineManager scriptEngineManager;
    @Inject private WebUtils webUtils;
    @Inject private MessageUtils messageUtils;

    public Eval() {
        super("?eval", "Admin command.", false);
        this.delete = false;
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        if (e.getAuthor().getIdLong() == Constants.PIGGYPIGLET && args.length == 1) {
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("Nashorn");
            scriptEngine.put("e", e);
            scriptEngine.put("zn", ZonedDateTime.now());
            scriptEngine.put("db", DB.getGlobalDatabase());

            try {
                e.getChannel().sendMessage("Eval: ```\n" + scriptEngine.eval(e.getMessage().getContentRaw().replace("?eval ", "")) + "```").queue();
            } catch (Exception ex) {
                e.getChannel().sendMessage("An exception was thrown: " + webUtils.hastebin(ex.getMessage())).queue();
            }
        }
    }
}
