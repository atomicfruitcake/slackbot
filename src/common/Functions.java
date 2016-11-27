package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;



/**
 * @author sambass
 *
 */
public class Functions {

	private static final Logger LOGGER = Logger.getLogger(Functions.class
			.getName());

	static File directory = null;

	public static String guiOutputMethod(int lineNumber) throws IOException {
		try {
			return Files.readAllLines(Paths.get("consoleout.txt")).get(
					lineNumber);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getConsoleOutput(int entryNo) {
		try {
			return guiOutputMethod(entryNo);
		} catch (IOException e) {
			throw new RuntimeException("Error fetching data from GUI output", e);
		}
	}

	public static String defaultReturner(String value, String defaultValue) {
		if ((value == null) || (value.isEmpty() == true)) {
			return defaultValue;
		} else {
			return value;
		}
	}

	public static String getSetting1() {
		return getConsoleOutput(0);
	}

	public static String getSetting2() {
		return getConsoleOutput(1);
	}


	public static void threadSleep(int x) {
		int time = x * 1000;
		try {
			Thread.sleep(time);
		} catch (InterruptedException a) {
			a.printStackTrace();
		}
	}
	
	public static void writeToTextFile(String text, String filename) {
		// Create text file if it does not exist and append text
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
				file.setReadable(true);
				file.setWritable(true);
			}
			FileWriter fw = new FileWriter(filename, true);
			fw.write(text + "\n");
			fw.close();

		} catch (IOException e) {
			LOGGER.info("Error creating text file: " + filename);
			e.printStackTrace();
		}
	}	
}