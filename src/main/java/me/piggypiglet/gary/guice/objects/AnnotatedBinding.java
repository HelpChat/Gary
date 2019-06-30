package me.piggypiglet.gary.guice.objects;

import lombok.Builder;
import lombok.Data;

import java.lang.annotation.Annotation;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
@Data @Builder
public final class AnnotatedBinding {
    private final Class clazz;
    private final Class<? extends Annotation> annotation;
    private final Object instance;
}