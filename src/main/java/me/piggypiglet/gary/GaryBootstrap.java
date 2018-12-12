package me.piggypiglet.gary;

import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class GaryBootstrap {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Class clazz = GaryBootstrap.class;

        BinderModule module = new BinderModule(clazz);
        Injector injector = module.createInjector();
        injector.injectMembers(clazz.newInstance());

        injector.getInstance(GaryBot.class).start(injector);
    }
}
