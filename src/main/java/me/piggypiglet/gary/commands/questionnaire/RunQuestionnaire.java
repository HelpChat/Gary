package me.piggypiglet.gary.commands.questionnaire;

import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.objects.enums.QuestionType;
import me.piggypiglet.gary.core.objects.questionnaire.Question;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;
import me.piggypiglet.gary.core.objects.questionnaire.Response;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RunQuestionnaire extends Command {
    @Inject private GaryBot garyBot;

    public RunQuestionnaire() {
        super("run");
        setArgPattern("-questionnaire", true);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        try {
            QuestionnaireBuilder builder = new QuestionnaireBuilder(e.getMember(), e.getChannel()).addQuestions(
                    new Question("questionnaire", "What questionnaire would you like to run?", QuestionType.STRING)
            );

            QuestionnaireBuilder.Questionnaire questionnaire = builder.build("I-Run-Questionnaire");
            String name = questionnaire.getResponses().get("questionnaire").getMessage().getContentRaw();
            Map<String, Response> responses = garyBot.getQuestionnaires().get(name).build(name).getResponses();

            e.getChannel().sendMessage(responses.values().toString()).queue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
