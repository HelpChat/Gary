package me.piggypiglet.gary.commands.standalone;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.Constants;
import me.piggypiglet.gary.core.objects.enums.QuestionType;
import me.piggypiglet.gary.core.objects.questionnaire.Question;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;
import me.piggypiglet.gary.core.storage.json.GFile;
import me.piggypiglet.gary.core.utils.http.HasteUtils;
import me.piggypiglet.gary.core.utils.string.StringUtils;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class EvalCommand extends Command {
    @Inject private ScriptEngineManager scriptEngineManager;
    @Inject private GFile gFile;

    public EvalCommand() {
        super("eval");
        argPattern = "eval-";
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (e.getAuthor().getIdLong() == Constants.PIGGYPIGLET) {
            TextChannel channel = e.getChannel();
            String code;

            if (args != null && args.length >= 1 && args[0].startsWith("\"") && args[0].endsWith("\"")) {
                code = StringUtils.replaceLast(args[0].replaceFirst("\"", ""), "\"", "");
            } else {
                code = getCode(e.getMember(), channel);
            }

            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("Nashorn");
            scriptEngine.put("e", e);
            scriptEngine.put("sys", System.out);
            scriptEngine.put("gFile", gFile);

            try {
                if (code.contains("gFile")) {
                    QuestionnaireBuilder.Questionnaire questionnaire = new QuestionnaireBuilder(e.getMember(), channel).addQuestions(
                            new Question("check", "Accessing gFile can expose confidential information, are you sure you want to execute this code?", QuestionType.EMOTE).setEmotes(
                                    e.getJDA(), "✅", "❎"
                            )
                    ).build("gi-temp");

                    if (!questionnaire.getResponses().get("check").getReaction().getReactionEmote().getName().equals("✅")) {
                        channel.sendMessage("Cancelling code evaluation.").queue();
                        return;
                    }
                }

                channel.sendMessage("Eval: ```\n" + scriptEngine.eval(code) + "```").queue();
            } catch (Exception ex) {
                channel.sendMessage("An exception was thrown: " + HasteUtils.haste(ex.getMessage())).queue();
            }
        }
    }

    private String getCode(Member member, TextChannel channel) {
        String code = getCodeQuestionnaire(member, channel).getResponses().get("code").getMessage().getContentRaw();

        while (!code.startsWith("```") && !code.endsWith("```")) {
            code = getCodeQuestionnaire(member, channel).getResponses().get("code").getMessage().getContentStripped();
        }

        return code.replace("```", "");
    }

    private QuestionnaireBuilder.Questionnaire getCodeQuestionnaire(Member member, TextChannel channel) {
        return new QuestionnaireBuilder(member, channel).addQuestions(
                new Question("code", "Please enter the code you'd like to execute (wrap in \\`\\`\\`code\\`\\`\\`).", QuestionType.STRING)
        ).build("gi-temp");
    }
}
