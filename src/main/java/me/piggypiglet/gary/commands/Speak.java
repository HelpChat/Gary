package me.piggypiglet.gary.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.piggypiglet.gary.core.framework.BinderModule;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.util.MessageUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public class Speak extends Command {
    @Inject private MessageUtil mutil;

    public Speak() {
        cmd = "1 /2 /3 /4 /5 /6 ";
        BinderModule module = new BinderModule(this.getClass());
        Injector injector = module.createInjector();
        injector.injectMembers(this);
    }

    public void execute(MessageReceivedEvent e, String[] args) {
        if (e.getChannel().getId().equalsIgnoreCase("373010561794834432")) {
            switch (e.getMessage().getContentRaw().charAt(0)) {
                case '1':
                    e.getGuild().getTextChannelById("164280494874165248").sendMessage(mutil.arrayToString(args).replace("1 ", "")).queue();
                    break;
                case '2':
                    e.getGuild().getTextChannelById("165129131770511360").sendMessage(mutil.arrayToString(args).replace("2 ", "")).queue();
                    break;
                case '3':
                    e.getGuild().getTextChannelById("339676414137860097").sendMessage(mutil.arrayToString(args).replace("3 ", "")).queue();
                    break;
                case '4':
                    e.getGuild().getTextChannelById("339773683469910016").sendMessage(mutil.arrayToString(args).replace("4 ", "")).queue();
                    break;
                case '5':
                    e.getGuild().getTextChannelById("382856648064237568").sendMessage(mutil.arrayToString(args).replace("5 ", "")).queue();
                    break;
                case '6':
                    e.getGuild().getTextChannelById("164523548390457355").sendMessage(mutil.arrayToString(args).replace("6 ", "")).queue();
                    break;
            }
        }
    }
}
