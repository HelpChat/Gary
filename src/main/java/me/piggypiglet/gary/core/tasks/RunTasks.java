package me.piggypiglet.gary.core.tasks;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import net.dv8tion.jda.core.JDA;

import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class RunTasks {
    @Inject private ChannelClearing cc;

    public RunTasks() {
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    public void setup(JDA jda) {
        cc.setup(jda);
    }

    public void runTasks() {
        System.out.println("Task - Request & Offer service cleaning started");
        long time = TimeUnit.DAYS.toMillis(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        Timer timer = new Timer();
        timer.schedule(cc, time, time);
    }
}
