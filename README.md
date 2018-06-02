![Gary](https://garys.life/Gary2.png)
[![Jenkins](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com/view/Precise/view/All%20Precise/job/precise-desktop-amd64_default.svg)](https://ci.piggypiglet.me/job/Gary/)<br/>
Hi! My name is Gary. I'm the bot that helps moderate [clip and funny's help chat](https://testplugins.com/discord).<br/>Currently, I do the following:
* Clear the RMS, Offer and Request service channels monthly.
* Enforce the format in the above channels.
* Be an awesome AI
* Assist staff with moderator commands
* Display placeholderapi info
* And more!

## For Developers
Gary has no documentation so you'll be looking around the codebase to find the right functions for things, so honestly, as much as I'd appreciate you contributing, you're probably better off telling me what you want added and waiting for me to add it.

If you have the patience and knowledge to figure out gary for yourself, go ahead and contribute. To make a command, add a new class in `me.piggypiglet.gary.commands` extending Command. Layout the class like this:
```java
package me.piggypiglet.gary.commands;

import me.piggypiglet.gary.core.framework.Command;

public final class CommandClass extends Command {

    public CommandClass() {
        super("?command", "description", true);
    }

    @Override
    protected void execute(MessageReceivedEvent e, String[] args) {
        // code
    }

}
```
The boolean in the super will specifiy whether the command will show up on the non staff commands list. By default, the command will delete itself once the execute method has ran, to disable this functionality, specify `this.delete = false;` under the super.<br/>
In Gary, we use google guice for dependency injection. This allows you to get an instance of a class with a simple annotation, for example:
```java
@Inject private ClassYouWantToInject classYouWantToInject;
```
Once you've made your command class, go into me.piggypiglet.gary.Gary and add an instance of your command class to the stream under "commands" in the register method.<br/><br/>Be sure to test your new class via your own server before making a pull request. Once you've tested and made the pull request, I'll review over your code and if accepted, your contributions will go live immediately.
