package me.piggypiglet.gary;

import com.google.inject.Injector;
import me.piggypiglet.gary.guice.modules.BindingSetterModule;
import me.piggypiglet.gary.registerables.Registerable;
import me.piggypiglet.gary.registerables.implementations.*;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
final class GaryBot {
    void start(Injector parentInjector) {
        AtomicReference<Injector> injector = new AtomicReference<>(parentInjector);

        Stream.of(
                FilesRegisterable.class,
                PterodactylRegisterable.class,
                SpigotRegisterable.class,
                JDARegisterable.class,
                CommandsRegisterable.class,
                ShutdownHookRegisterable.class,
                ConsoleRegisterable.class
        ).forEach(r -> {
            Registerable registerable = injector.get().getInstance(r);
            registerable.run();

            if (registerable.getProviders().size() > 0 || registerable.getAnnotatedBindings().size() > 0 || registerable.getStaticInjections().size() > 0) {
                injector.set(injector.get().createChildInjector(new BindingSetterModule(
                        registerable.getProviders(),
                        registerable.getAnnotatedBindings(),
                        registerable.getStaticInjections().toArray(new Class[]{})
                )));
            }
        });

        LoggerFactory.getLogger("Gary").info("Startup done");
    }
}
