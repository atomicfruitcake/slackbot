package main;

import static common.Properties.SLACK_BOT_ERROR;
import static common.Properties.SLACK_BOT_TOKEN;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

/**
 * @author atomicfruitcake
 *
 */
public class SlackBot {
    private static final Logger LOGGER = Logger.getLogger(SlackBot.class
	    .getName());

    static String[] commands;
    static SlackSession session;
    static SlackChannel slackChannel;

    // Creates a webSocket connection to a Slack instance
    public static void slackSession() throws IOException {
	LOGGER.info("Creating websocket connection to slack");
	session = SlackSessionFactory
		.createWebSocketSlackSession(SLACK_BOT_TOKEN);
	session.connect();
	LOGGER.info("Successfully created connection to slack");
    }

    // Registered a listener to slack channels to see if slackbot is called with
    // @ is called. Note if the channel is private then the slackbot must have
    // been invited to the channel
    public static void registerAListener() throws IOException {
	SlackMessagePostedListener messagePostedListener = new SlackMessagePostedListener() {
	    @Override
	    public void onEvent(SlackMessagePosted event, SlackSession session) {
		try {
		    slackChannel = event.getChannel();
		} catch (Exception e) {
		    LOGGER.info("ERROR GETTING SLACK CHANNEL");
		    e.printStackTrace();
		}
		if (event.getMessageContent().contains("testbotID")) {
		    runTestbotSlackOnEvent(event);
		}
	    }
	};
	session.addMessagePostedListener(messagePostedListener);
    }

    // disconnects the websocket connection to slack
    public static void disconnectSlackSession() {
	try {
	    LOGGER.info("Closing connection to Slack");
	    session.disconnect();
	} catch (IOException e) {
	    LOGGER.info("Error closing connection to Slack");
	    e.printStackTrace();
	}
    }

    // Sends a message to a slack channel
    public static void sendMessageToAChannel(String message) throws IOException {
	LOGGER.info("Sending " + message + " to slack channel "
		+ slackChannel.getName());
	try {
	    session.sendMessageOverWebSocket(slackChannel, message);
	} catch (Exception e) {
	    LOGGER.info("Failed to send slack Message");
	    e.printStackTrace();
	}

    }

    // Sends a message to a user
    public static void sendDirectMessageToAUser(String message, String user)
	    throws IOException {
	LOGGER.info("Sending " + message + " to slack user " + user);
	SlackUser slackUser = session.findUserByUserName(user);
	session.sendMessageToUser(slackUser, message, null);
    }

    // Sends a message with an attachment to channel
    public static void sendUsingPreparedMessage(String message, String channel)
	    throws IOException {
	SlackPreparedMessage preparedMessage = new SlackPreparedMessage.Builder()
		.withMessage(message).withUnfurl(false)
		.addAttachment(new SlackAttachment())
		.addAttachment(new SlackAttachment()).build();
	session.sendMessage(session.findChannelByName(channel), preparedMessage);
    }

    // Fetches the most recent message from a channel
    public static String fetchMostRecentMessageFromChannelHistory(
	    SlackMessagePosted event) throws IOException {
	LOGGER.info("Fetching most recent message from channel "
		+ slackChannel.getName());
	return event.getMessageContent();

    }

    // Splits a slack message by spaces
    public static String[] splitSlackMessageForTestbot(String slackMessage) {
	try {
	    return slackMessage.split("\\s+");
	} catch (Exception e) {
	    LOGGER.info("Slack command did not include sufficient information");
	    e.printStackTrace();
	    return null;
	}
    }

    public static void readSlackMessage(String slackMessage) {
	String[] splitMessage = splitSlackMessageForTestbot(slackMessage);
	for (int i = 0; i <= splitMessage.length; i++) {

	    try {
		commands[i] = splitMessage[i].toLowerCase();
	    } catch (Exception e) {
		commands[i] = null;
	    }
	}
    }

    // Responds to a user who talks to @testbot
    public static void sendResponseToSlack(String slackMessage)
	    throws IOException {
	LOGGER.info("Responding to Slack Message : " + slackMessage);
	readSlackMessage(slackMessage);
	switch (commands[0]) {

	case "foo": {
	    sendMessageToAChannel("bar");
	    break;
	}

	case "hi": {
	    sendMessageToAChannel("Hi");
	    break;
	}

	case "call": {
	    sendMessageToAChannel("Response");
	    break;
	}

	case "help": {
	    sendMessageToAChannel("Enter help message here");
	    break;
	}

	case "reset": {
	    sendMessageToAChannel("Resetting my settings, let's hope this fixes me");
	    SlackBotTestRunner.resetSlackTestBot();
	    sendMessageToAChannel("I've successfully reset myself");
	    break;
	}
	default: {
	}
	    sendMessageToAChannel(SLACK_BOT_ERROR[(int) Math.random()
		    * SLACK_BOT_ERROR.length]);
	    break;
	}
    }

    // Runs the testbot when a listener hears that @testbot is called
    public static void runTestbotSlackOnEvent(SlackMessagePosted event) {
	try {
	    LOGGER.info("Responding to message on Slack");
	    sendResponseToSlack(fetchMostRecentMessageFromChannelHistory(event));
	} catch (IOException e) {
	    e.printStackTrace();
	    try {
		sendResponseToSlack("Enter help message here");
	    } catch (IOException f) {
		LOGGER.info("Test bot did not understand command, sending help message");
		f.printStackTrace();
	    }
	    LOGGER.severe("Unable to run the testbot");

	}
    }
}