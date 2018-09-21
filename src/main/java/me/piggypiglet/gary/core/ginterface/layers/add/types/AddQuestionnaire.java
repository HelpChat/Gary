package me.piggypiglet.gary.core.ginterface.layers.add.types;

import com.google.inject.Inject;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.ginterface.layers.add.AddAbstract;
import me.piggypiglet.gary.core.objects.enums.ginterface.types.AddType;
import me.piggypiglet.gary.core.objects.questionnaire.Question;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;
import me.piggypiglet.gary.core.objects.questionnaire.Response;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class AddQuestionnaire extends AddAbstract {
    @Inject private GaryBot garyBot;

    public AddQuestionnaire() {
        super(AddType.QUESTIONNAIRE);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e) {
        //todo cleanup this class

        try {
            Member member = e.getMember();
            TextChannel channel = e.getChannel();

            QuestionnaireBuilder builder = new QuestionnaireBuilder(member, channel).addQuestions(
                    new Question("name", "What do you want to name this questionnaire?", "str"),
                    new Question("questions", "How many questions do you want?", "str")
            );

            Map<String, Response> info = builder.build().getResponses();
            int iterations = Integer.parseInt(info.get("questions").getMessage().getContentRaw());
            List<Question> questions = new ArrayList<>();

            for (int i = 0; i < iterations; ++i) {
                QuestionnaireBuilder questionBuilder = new QuestionnaireBuilder(member, channel).addQuestions(
                        new Question("question", "What question would you like to ask?", "str"),
                        new Question("key", "What identifier would you like to use for this question?", "str"),
                        new Question("acceptable answers", "What do you want users to respond with (`str` or list emotes, split with |.)?", "str")
                );

                QuestionnaireBuilder.Questionnaire questionBuilderQuestionnaire = questionBuilder.build();
                Map<String, Response> questionBuilderResponses = questionBuilderQuestionnaire.getResponses();

                String question = questionBuilderResponses.get("question").getMessage().getContentRaw();
                String key = questionBuilderResponses.get("key").getMessage().getContentRaw();
                Object[] acceptableAnswers = questionBuilderResponses.get("acceptable answers").getMessage().getContentRaw().split("\\|");

                questions.add(new Question(key, question, acceptableAnswers));
            }

            garyBot.getQuestionnaires().put(info.get("name").getMessage().getContentRaw(), new QuestionnaireBuilder(member, channel).addQuestions(questions.toArray(new Question[]{})));
            e.getChannel().sendMessage("Questionnaire successfully made.").queue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
