package me.piggypiglet.gary.core.ginterface.layers.add.types;

import me.piggypiglet.gary.core.ginterface.layers.add.AddAbstract;
import me.piggypiglet.gary.core.objects.enums.ginterface.types.AddType;
import me.piggypiglet.gary.core.objects.questionnaire.Question;
import me.piggypiglet.gary.core.objects.questionnaire.QuestionnaireBuilder;
import me.piggypiglet.gary.core.objects.questionnaire.Response;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class AddQuestionnaire extends AddAbstract {
    public AddQuestionnaire() {
        super(AddType.QUESTIONNAIRE);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e) {
        System.out.println(Thread.currentThread().getName());

        Member member = e.getMember();
        TextChannel channel = e.getChannel();

        QuestionnaireBuilder builder = new QuestionnaireBuilder(member, channel).addQuestions(
                new Question("name", "What do you want to name this questionnaire?", "str"),
                new Question("questions", "How many questions do you want?", "str")
        );

        Map<String, Response> info = builder.build().getResponses();


    }
}
