package main;

import static common.Properties.DEFAULT_SETTINGS;
import static common.Properties.SETTINGS_FILE;
import static common.Properties.CHUCK_NORRIS_JOKE_API;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.testng.Assert;

import common.Functions;

/**
 * @author atomicfruitcake
 *
 */
/*
 * This class contains the various functions than can be executed based upon
 * commands sent to the slackbot
 */
public class SlackBotFunctions {

    static String slackTestPack;
    static String slackTestEnvironment;

    private static final Logger LOGGER = Logger
	    .getLogger(SlackBotFunctions.class.getName());

    // Resets the settings.txt settings
    public static void resetSlackBot() {
	LOGGER.info("Resetting testbot settings");
	String filename = SETTINGS_FILE;
	File file = new File(filename);
	file.delete();
	Functions.writeToTextFile(DEFAULT_SETTINGS, filename);
    }

    // Resets settings and turns slackbot off and on again
    public static void hardResetSlackBot() throws IOException {
	resetSlackBot();
	SlackBot.disconnectSlackSession();
	SlackBot.reconnectSlackSession();
    }

    // Gets a Random Chuck Norris Joke
    public static String getChuckNorrisJoke() throws MalformedURLException,
	    IOException {
	LOGGER.info("Getting Chuck Norris joke from API");
	HttpURLConnection connection = (HttpURLConnection) new URL(
		CHUCK_NORRIS_JOKE_API).openConnection();
	connection.setRequestMethod("GET");
	BufferedReader bufferedReader = new BufferedReader(
		new InputStreamReader(connection.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();
	while ((inputLine = bufferedReader.readLine()) != null) {
	    response.append(inputLine);
	}
	bufferedReader.close();
	JSONObject jsonObject = (JSONObject) JSONValue.parse(response
		.toString());
	JSONObject jsonObjectJokeValue = (JSONObject) jsonObject.get("value");
	return (String) jsonObjectJokeValue.get("joke");
    }

    // Simple calculator app for slack
    public static String slackBotCalc(String command, String num1, String num2,
	    String operation) throws IOException {
	try {
	    switch (command) {
	    case "add": {
		Assert.assertEquals(operation, "+");
		return Integer.toString((Integer.parseInt(num1) + Integer
			.parseInt(num2)));
	    }
	    case "minus": {
		Assert.assertEquals(operation, "-");
		return Integer.toString((Integer.parseInt(num1) - Integer
			.parseInt(num2)));

	    }
	    case "multiply": {
		Assert.assertEquals(operation, "*");
		return Integer.toString((Integer.parseInt(num1) * Integer
			.parseInt(num2)));
	    }
	    case "divide": {
		Assert.assertEquals(operation, "/");
		return Integer.toString((Integer.parseInt(num1) / Integer
			.parseInt(num2)));
	    }
	    default: {
		return null;
	    }
	    }
	} catch (Exception e) {
	    SlackBot.sendMessageToAChannel("Sorry, I don't understand, I need the following format \n"
		    + command
		    + "_x_"
		    + operation
		    + "_y_"
		    + " : where _x_ and _y_ are integers");
	    LOGGER.info("Error Using slackBot Calculator");
	    e.printStackTrace();
	    return null;
	}
    }
}
