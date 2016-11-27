package main;

import java.util.logging.Logger;

/**
 * @author atomicfruitcake
 *
 */

public class RunSlackBot {
    
    private static final Logger LOGGER = Logger
	    .getLogger(RunSlackBot.class.getName());
    
    public static void main() {
	try {
	    SlackBot.slackSession();
	    SlackBot.registerAListener();
	    while (true) {
		Thread.yield();
	    }

	} catch (Exception e) {
	    LOGGER.info("Unable to start Slackbot");
	    e.printStackTrace();
	}

    }
}
