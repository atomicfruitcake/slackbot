package common;

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
    
    public static final String SLACK_WEBHOOK = "";
    public static final String SLACK_BOT_TOKEN = "";
    public static final String SLACK_BOT_CHANNEL = "testbot";
    public static final String SLACK_BOT_ID = "";
    public static final String[] SLACK_BOT_CONFIRMATION = new String[] {
	    "Sure thing, ", "I'm on it, ", "No problem, " };
    public static final String[] SLACK_BOT_ERROR = new String[] {
	    "I'm sorry, I don't understand that, try _slackbot help_",
	    "I don't get that. Maybe try _slackbot help_" };

}
