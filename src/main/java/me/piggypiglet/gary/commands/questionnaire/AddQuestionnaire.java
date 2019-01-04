package me.piggypiglet.gary.commands.questionnaire;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.commands.Command;
import me.piggypiglet.gary.core.handlers.misc.QuestionnaireHandler;
import me.piggypiglet.gary.core.objects.enums.QuestionType;
import me.piggypiglet.gary.core.objects.enums.Roles;
import me.piggypiglet.gary.core.objects.questionnaire.Question;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;
import me.piggypiglet.gary.core.objects.questionnaire.Response;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class AddQuestionnaire extends Command {
    @Inject private QuestionnaireHandler questionnaireHandler;

    public AddQuestionnaire() {
        super("questionnaire add", "questionnaire make", "questionnaire create");
        options.setRole(Roles.TRUSTED).setDescription("Create a questionnaire with multiple choice or text based questions.");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        Member member = e.getMember();
        TextChannel channel = e.getChannel();

        QuestionnaireBuilder.Questionnaire questionnaire = new QuestionnaireBuilder(member, channel).addQuestions(
                new Question("name", "What do you want to name this questionnaire?", QuestionType.STRING),
                new Question("questions", "How many questions do you want?", QuestionType.INT)
        ).build("temp");

        Map<String, Response> info = questionnaire.getResponses();
        List<Question> questions = new ArrayList<>();
        int iterations = info.get("questions").getInteger();

        for (int i = 0; i < iterations; ++i) {
            QuestionnaireBuilder questionBuilder = new QuestionnaireBuilder(member, channel).addQuestions(
                    new Question("question", "What question would you like to ask?", QuestionType.STRING),
                    new Question("key", "What identifier would you like to use for this question?", QuestionType.STRING),
                    new Question("answer", "What do you want users to respond with (string, emote, int)?", QuestionType.STRING)
            );

            Map<String, Response> questionBuilderResponses = questionBuilder.build("temp-question-builder").getResponses();

            String question = questionBuilderResponses.get("question").getMessage().getContentRaw();
            String key = questionBuilderResponses.get("key").getMessage().getContentRaw();
            QuestionType acceptableAnswer = QuestionType.valueOf(questionBuilderResponses.get("answer").getMessage().getContentRaw().toUpperCase());
            Question questionObj = new Question(key, question, acceptableAnswer);

            if (acceptableAnswer == QuestionType.EMOTE) {
                QuestionnaireBuilder emotes = new QuestionnaireBuilder(member, channel).addQuestions(
                        new Question("emotes", "What emotes do you want the user to pick from (Split emotes with |)?", QuestionType.STRING)
                );

                questionObj.setEmotes(channel.getJDA(), (Object[]) emotes.build("temp-emotes").getResponses().get("emotes").getMessage().getContentRaw().split("\\|"));
            }

            questions.add(questionObj);
        }

        questionnaireHandler.getQuestionnaires().put(info.get("name").getMessage().getContentRaw(), new QuestionnaireBuilder(member, channel).addQuestions(questions.toArray(new Question[]{})));
        e.getChannel().sendMessage("Questionnaire successfully made.").queue();
    }
}
