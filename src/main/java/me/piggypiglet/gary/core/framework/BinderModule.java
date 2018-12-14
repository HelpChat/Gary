package me.piggypiglet.gary.core.framework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import me.piggypiglet.gary.GaryBot;
import me.piggypiglet.gary.core.objects.tasks.Task;
import me.piggypiglet.gary.core.storage.file.Lang;
import me.piggypiglet.gary.core.utils.http.HasteUtils;
import me.piggypiglet.gary.core.utils.misc.PunishmentUtils;
import me.piggypiglet.gary.core.utils.mysql.MessageUtils;

import javax.annotation.Nonnull;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class BinderModule extends AbstractModule {
    private final Class clazz;

    public BinderModule(Class clazz) {
        this.clazz = clazz;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure() {
        bind(clazz).toInstance(clazz);
        requestStaticInjection(HasteUtils.class, MessageUtils.class, Task.class, Lang.class, PunishmentUtils.class, GaryBot.class);
    }

    @Provides
    @Nonnull
    public Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        return builder.create();
    }
}
