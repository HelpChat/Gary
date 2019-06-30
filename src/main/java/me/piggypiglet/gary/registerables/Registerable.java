package me.piggypiglet.gary.registerables;

import lombok.Getter;
import me.piggypiglet.gary.guice.objects.AnnotatedBinding;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public abstract class Registerable {
    @Getter private final List<Object> providers = new ArrayList<>();
    @Getter private final List<AnnotatedBinding> annotatedBindings = new ArrayList<>();
    @Getter private final List<Class> staticInjections = new ArrayList<>();

    protected abstract void execute();

    // TODO: provide injector back into registerable and inject members
    protected void addBinding(Object instance) {
        providers.add(instance);
    }

    protected void addAnnotatedBinding(Class interfaze, Class<? extends Annotation> annotation, Object instance) {
        annotatedBindings.add(AnnotatedBinding.builder()
                .clazz(interfaze)
                .annotation(annotation)
                .instance(instance)
                .build());
    }

    protected void requestStaticInjections(Class... classes) {
        staticInjections.addAll(Arrays.asList(classes));
    }

    public void run() {
        execute();
    }
}
