package me.piggypiglet.gary.conversation;

import lombok.Builder;
import lombok.Data;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@Data
public final class Conversation {
    private final Member member;
    private final TextChannel textChannel;
    private final List<Prompt> prompts;
    private final Map<String, String> completedAnswers = new HashMap<>();
    private final CompletableFuture<Map<String, String>> answers = new CompletableFuture<>();


    @Builder
    public Conversation(Member member, TextChannel textChannel, List<Prompt> prompts) {
        this.member = member;
        this.textChannel = textChannel;
        this.prompts = prompts;
    }

    public boolean match(Member member, TextChannel channel) {
        return member.getId().equals(this.member.getId()) &&
                channel.getId().equals(textChannel.getId());
    }
}
