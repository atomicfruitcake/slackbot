package main;

import static common.Properties.DEFAULT_SETTINGS;
import static common.Properties.SETTINGS_FILE;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

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

    public static boolean testStop;
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
}
