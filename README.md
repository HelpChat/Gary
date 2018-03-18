# Gary

[![Jenkins](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com/view/Precise/view/All%20Precise/job/precise-desktop-amd64_default.svg)](https://ci.piggypiglet.me/job/Gary/)

Hi! My name is Gary. I'm the bot that helps moderate [clip and funny's help chat](https://testplugins.com/discord)<br/>Currently, I do the following:
* Clear the RMS, Offer and Request service channels monthly.
* Enforce the format in the above channels.
* Be an awesome AI
* Assist trusted/helpers with moderator commands
* Display placeholderapi info
* And more!

## For Developers
If you are looking to contribute to gary, it's super simple! To make a command, add a new class in `me.piggypiglet.gary.commands` extending Command. Layout the class like this:
```java
package me.piggypiglet.gary.commands;

import me.piggypiglet.gary.core.framework.Command;

public final class CommandClass extends Command {

    public CommandClass() {
        super("?command");
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        // code
    }

}
```
In Gary, we use google guice for dependency injection. This allows you to get an instance of a class with a simple annotation, for example:
```java
@Inject private ClassYouWantToInject classYouWantToInject;
```
Once you've made your command class, be sure to test it via your own server before making a pull request. Once you've tested and made the pull request, I'll review over you code and if accepted, your contributions will go live immediately.
