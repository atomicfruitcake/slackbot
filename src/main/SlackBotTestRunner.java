package main;

import static common.Properties.DEFAULT_SETTINGS;
import static common.Properties.SETTINGS_FILE;

import java.io.File;
import java.util.logging.Logger;

import common.Functions;


/**
 * @author atomicfruitcake
 *
 */
/*
 * This class contains the various functions than can be executed based upon commands sent to the slackbot
 */
public class SlackBotTestRunner {

    public static boolean testStop;
    static String slackTestPack;
    static String slackTestEnvironment;

    private static final Logger LOGGER = Logger
	    .getLogger(SlackBotTestRunner.class.getName());

    // Resets the consoleout.txt settings
    public static void resetSlackTestBot() {
	LOGGER.info("Resetting testbot settings");
	String filename = SETTINGS_FILE;
	File file = new File(filename);
	file.delete();
	Functions.writeToTextFile(DEFAULT_SETTINGS, filename);
    }
}
