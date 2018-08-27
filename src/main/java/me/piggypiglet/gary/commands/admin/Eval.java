package me.piggypiglet.gary.commands.admin;

import co.aikar.idb.DB;
import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.utils.message.StringUtils;
import me.piggypiglet.gary.core.utils.web.WebUtils;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

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
    @Inject private StringUtils stringUtils;

    public Eval() {
        super("?eval", "Admin command.", false);
        this.delete = false;
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (e.getAuthor().getIdLong() == Constants.PIGGYPIGLET && args.length == 1) {
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("Nashorn");
            TextChannel channel = e.getChannel();

            scriptEngine.put("e", e);
            scriptEngine.put("zn", ZonedDateTime.now());
            scriptEngine.put("db", DB.getGlobalDatabase());

            try {
                channel.sendMessage("Eval: ```\n" + scriptEngine.eval(e.getMessage().getContentRaw().replace("?eval ", "")) + "```").queue();
            } catch (Exception ex) {
                channel.sendMessage("An exception was thrown: " + webUtils.hastebin(ex.getMessage())).queue();
            }
        }
    }
}
