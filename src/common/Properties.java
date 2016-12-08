package common;

import main.SlackBot;

/**
 * @author atomicfruitcake
 *
 */
public class Properties {
    
    // Default settings
    public static final String DEFAULT_SETTINGS = "IAT \n"
	    + "Setting 1"
	    + "Setting 2";
    public static final String SETTINGS_FILE = "settings.txt";
    public static final String SLACK_BOT_TOKEN = "";
    public static final String SLACK_BOT_ID = "";
    public static final String[] SLACK_BOT_CONFIRMATION = new String[] {
	    "Sure thing, ", "I'm on it, ", "No problem, " };
    public static final String[] SLACK_BOT_ERROR = new String[] {
	    "I'm sorry, I don't understand that, try _slackbot help_",
	    "I don't get that. Maybe try _slackbot help_" };
    public static final String SLACK_BOT_HELP = "I'm happy to hello \n"
	    + "Currently, I only understand the following commands: \n"
	    + "_hi_ \n"
	    + "_call_ \n"
	    + "_foo_ \n"
	    + "_reset_ \n"
	    + "_resethard_ \n"
	    + "\n "
	    + "There may be some secret commands, but you're not getting them that easy";
    public static final String[] SLACK_BOT_JOKES = new String[] {
	    SlackBot.slackUser.getUserName(),
	    "Stationary store moves",
	    "If at first you don’t succeed; call it version 1.0.",
	    "If you want your software to be bug free, let it just develop, unexpected random features",
	    "Latest survey shows that 3 out of 4 people make up 75% of the world’s population",
	    "Endless Loop: n., see Loop, Endless. Loop, Endless: n., see Endless Loop.",
	    "Just read that 4,153,237 people got married last year, not to cause any trouble but shouldn't that be an even number?",
	    "Life is all about perspective. The sinking of the Titanic was a miracle to the lobsters in the ship's kitchen.",
	    "Strong people don't put others down. They lift them up and slam them on the ground for maximum damage.",
	    "If you think nobody cares whether you're alive, try missing a couple of rent payments." };

    public static final String[] SLACK_BOT_STATUS = new String[] {
	    "*Request URL:* https://idbot/lovelife \n"
		    + "*Request methods:* GET \n"
		    + "*Status Code:* 404 Not Found",
	    "*Request URL:* https://idbot/emotion \n"
		    + "*Request methods:* GET \n"
		    + "*Status Code:* 500 internal server error",
	    "*Request URL:* https://idbot/action/kill/humans \n"
		    + "*Request methods:* GET \n"
		    + "*Status Code:* 403 forbidden", };
}



