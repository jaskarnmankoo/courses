package _backend.utils.errorHandling;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ErrorLogger {
	private static final Logger logger = Logger.getLogger(ErrorLogger.class.getName());
	private static FileHandler fh = null;

	// We can decide on the final file path later
	private static String filePath;

	// Singleton pattern. There should be only one error logger.
	private static ErrorLogger EL = new ErrorLogger();

	public ErrorLogger() {
		// Need to save

		// Lets get the native home directory
		String homeDir = System.getProperty("user.home");
		// First check for the folder
		Path folderPath = Paths.get(String.format("%s/Trackr", homeDir));
		File folder = folderPath.toFile();
		if (!folder.exists()) { // Checks for the existence of the folder
			folder.mkdir();
		}
		folderPath = Paths.get(String.format("%s/Trackr/Logs", homeDir));
		// Create the logs folder if it doesn't exist yet
		folder = folderPath.toFile();
		if (!folder.exists()) { // Checks for the existence of the folder
			folder.mkdir();
		}
		filePath = String.format("%s/Trackr/Logs/errors.log", homeDir);

		// We have to make the file writable here for the logger to actually write to
		// the file.
		File file = new File(filePath);
		file.setWritable(true);

		// This creates a simple and easy-to-read format for our logger
		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
		try {
			fh = new FileHandler(filePath, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);

		// We've already sync'd the file with the logger, so we can set the file itself
		// to read-only.
		// It can only be written to from the logger now.
		file.setWritable(false);

		// This makes it so we don't log any information to the console.
		// You can comment this line if you want the logs to show up on console.
		logger.setUseParentHandlers(false);
	}

	// Used to simply log any message.
	public static void logMessage(String message) {
		logger.info(message + "\n");
	}

	// Used specifically for exceptions.
	public static void logError(Exception e) {
		// Go up through the stack trace to the original class and method that gave this
		// exception
		Throwable rootCause = e;
		while (rootCause.getCause() != null && rootCause.getCause() != rootCause)
			rootCause = rootCause.getCause();

		String result = e.toString() + "\n";

		// If I just add the ".getClassName()", I get the whole path to the class
		// (separated by a "." between each folder)
		String[] classPackage = rootCause.getStackTrace()[0].getClassName().split("\\.");
		// Get the class name without the package path attached to it
		String className = classPackage[classPackage.length - 1];

		// Uncomment this next line and you'll see what I mean
		// result += "Class: " + rootCause.getStackTrace()[0].getClassName() + "\n";
		result += "Class: " + className + "\n";
		result += "Method: " + rootCause.getStackTrace()[0].getMethodName() + "\n";

		// Record this result in the logger
		logger.info(result);

		System.err.println(result);
	}
}
