![Gary](https://helpch.at/resources/gary_header.png)
[![Jenkins](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com/view/Precise/view/All%20Precise/job/precise-desktop-amd64_default.svg)](https://ci.piggypiglet.me/job/Gary/)<br/>test
Hi! My name is Gary. I'm the bot that helps moderate [clip and funny's help chat](https://testplugins.com/discord).<br/>Currently, I do the following:
* Clear the RMS, Offer and Request service channels monthly.
* Enforce the format in the above channels.
* Prevent bumping in the above channels.
* Run the Role Request system.
* Provide logs for staff.
* Prevent people from spamming/raiding the server.

## For Developers
Gary's framework is only comparable to spaghetti, but if you wish to ignore that and try to add on to it, just a few things to keep in mind. Always attempt to follow SOLID, OOP & DRY to the best of your ability, and look through the util classes/objects as there's things in there premade to make life easier.

Gary uses guice along with org.reflections to automatically find any classes that extend specific classes, eg command and event classes. This means that you can simply make a new event class, and you don't have to manually add it's instance to jda.

If the above has discouraged you from contributing, remember that ideas are always welcome. Feel free to tag @PiggyPiglet in the [discord](https://helpch.at/discord) with anything you think should be added to gary.
