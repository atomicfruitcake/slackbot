package main;

import static common.Properties.SLACK_BOT_ERROR;
import static common.Properties.SLACK_BOT_HELP;
import static common.Properties.SLACK_BOT_ID;
import static common.Properties.SLACK_BOT_TOKEN;
import static common.Properties.SLACK_BOT_JOKES;
import static common.Properties.SLACK_BOT_STATUS;

import java.io.IOException;
import java.util.logging.Logger;

import org.testng.Assert;

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

    static SlackSession session;
    static SlackChannel slackChannel;
    public static SlackUser slackUser;
    static String[] splitMessage;
    static String command;

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
		    LOGGER.info("Error reading slack channel");
		    e.printStackTrace();
		}
		if (event.getMessageContent().contains(SLACK_BOT_ID)) {
		    slackUser = event.getSender();
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

    // Reconnects to an existing session
    public static void reconnectSlackSession() {
	try {
	    session.connect();
	} catch (IOException e) {
	    LOGGER.info("Error reconnecting to Slack");
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
	    System.out.println("SLACK MESSAGE IS : " + slackMessage);
	    return slackMessage.split("\\s+");
	} catch (Exception e) {
	    LOGGER.info("Slack command did not include sufficient information");
	    e.printStackTrace();
	    return null;
	}
    }

    // Sends a response to slack channel based on commands sent to slackbot
    public static void readSlackMessage(String slackMessage) {
	LOGGER.info("Responding to Slack Message : " + slackMessage);

	splitMessage = splitSlackMessageForTestbot(slackMessage);
	try {
	    command = splitMessage[1].toLowerCase();
	} catch (Exception e) {
	    command = null;
	}
    }

    // Responds to a user who talks to @testbot
    public static void sendResponseToSlack(String slackMessage)
	    throws IOException {
	LOGGER.info("Responding to Slack Message : " + slackMessage);
	readSlackMessage(slackMessage);
	switch (command) {

	case "foo": {
	    sendMessageToAChannel("bar");
	    break;
	}

	case "hi": {
	    sendMessageToAChannel("Hi " + slackUser.getUserName() + " :wave:");
	    break;
	}

	case "help": {
	    sendMessageToAChannel(SLACK_BOT_HELP);
	    break;
	}

	case "reset": {
	    sendMessageToAChannel("Resetting my settings, let's hope this fixes me");
	    SlackBotFunctions.resetSlackBot();
	    sendMessageToAChannel("I've successfully reset myself");
	    break;
	}

	case "resethard": {
	    sendMessageToAChannel("Performing Reset 2: Reset Harder \n"
		    + "Turning myself off and on again");
	    SlackBotFunctions.hardResetSlackBot();
	    sendMessageToAChannel("I've successfully turned myself off and on again");
	    break;
	}

	case "reverse": {
	    sendMessageToAChannel(new StringBuilder(splitMessage[2]).reverse()
		    .toString());
	    break;
	}

	case "add": {
	    try {
		Assert.assertEquals(splitMessage[3], "+");
		sendMessageToAChannel(Integer.toString((Integer
			.parseInt(splitMessage[2]) + Integer
			.parseInt(splitMessage[4]))));
		break;
	    } catch (Exception e) {
		LOGGER.severe("Failed to read " + command + " command");
		e.printStackTrace();
		break;
	    }
	}

	case "minus": {
	    try {
		Assert.assertEquals(splitMessage[3], "-");
		sendMessageToAChannel(Integer.toString((Integer
			.parseInt(splitMessage[2]) - Integer
			.parseInt(splitMessage[4]))));
		break;
	    } catch (Exception e) {
		LOGGER.severe("Failed to read " + command + " command");
		e.printStackTrace();
		break;
	    }
	}

	case "multiply": {
	    try {
		Assert.assertEquals(splitMessage[3], "*");
		sendMessageToAChannel(Integer.toString((Integer
			.parseInt(splitMessage[2]) * Integer
			.parseInt(splitMessage[4]))));
		break;
	    } catch (Exception e) {
		LOGGER.severe("Failed to read " + command + " command");
		e.printStackTrace();
		break;
	    }
	}

	case "divide": {
	    try {
		Assert.assertEquals(splitMessage[3], "/");
		sendMessageToAChannel(Integer.toString((Integer
			.parseInt(splitMessage[2]) / Integer
			.parseInt(splitMessage[4]))));
		break;
	    } catch (Exception e) {
		LOGGER.severe("Failed to read " + command + " command");
		e.printStackTrace();
		break;
	    }
	}

	case "chucknorris": {
	    sendMessageToAChannel(SlackBotFunctions.getChuckNorrisJoke());
	    break;
	}

	case "dude": {
	    sendMessageToAChannel("sweet");
	    break;
	}

	case "sweet": {
	    sendMessageToAChannel("dude");
	    break;
	}

	case "tellmeajoke": {
	    sendMessageToAChannel(SLACK_BOT_JOKES[(int) (Math.random() * SLACK_BOT_JOKES.length)]);
	    break;
	}

	case "howareyou": {
	    sendMessageToAChannel(SLACK_BOT_STATUS[(int) (Math.random() * SLACK_BOT_STATUS.length)]);
	    break;
	}

	default: {
	    sendMessageToAChannel(SLACK_BOT_ERROR[1 + (int) ((Math.random() * SLACK_BOT_ERROR.length))]);
	    break;
	}
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
