# slackbot

## What is slackbot?

Slackbot allows you quickly set up a chatbot in slack and configure it to do whatever you want. 
Slackbot was born out of chatops (think running jenkins jobs through a slack channel). 
Slackbot generalises that idea, based on a given input where slackbot is listening, you can make it do whatever you want.
It could something a simple as replying back with useful information or someone to talk too if you are lonely.
It could be something more complex like running test suites or deploying your code to new environment. 

## Getting Started
To get started with slackbot, download/clone this repo and build it with gradle (a wrapper is included if required). You can also set slackbot to work with your chosen IDE, Eclipse and idea are supported. Once built you will need to get a bot token from slack. This can be found by navigating to https://<YOUR_SLACK_INSTANCE>.slack.com/apps/build/custom-integration and selecting a bot. This will give a token which needs to be inserted into the **src.common.Properties.java** file. If you want your slackbot to work on any private channels make sure to invite it into the channel. Once setup, you can start the slackbot by running the **src.main.RunSlackBot.java** file. Once the bot is listening, message the slackbot directly and check that you can see the message through the listener on the console output. This will give you your slackbot ID that needs to be placed inside the properties file. Stop the bot from running and you're good to go.  

You can check that the bot is working by sending the command foo to the bot through slack when it is running, e.g. 
```
@slackbot foo
```

The slackbot should respond with 'bar'. There are a few other simple commands preconfigured to test that it is working

## Building new commands
Building new commands with slackbot is done in the **src/main/SlackBot.java** file. The sendResponseToSlack is where all the commands are seperated as cases. For example if I wanted the slack bot to send a *reponse* when I sent it a *call*, I would add the following case to the switch.

```java
case "call": {
	    sendMessageToAChannel("response");
	    break;
	}
```

It is inside this case where you implement for complex tasks for the slackbot perform. The slackbot command can be appended by any number of flags or messages. For example, you could have a input such as:

```
@slackbot deploy 1.0.0 to production
```
Where the version and environment are saved as flags inside the ```splitMessage[]``` string array. 