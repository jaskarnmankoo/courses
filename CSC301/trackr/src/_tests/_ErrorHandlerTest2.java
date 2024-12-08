package _tests;

import _backend.utils.errorHandling.ErrorLogger;

public class _ErrorHandlerTest2 {
	public static void logFromHere() {
		ErrorLogger.logError(new Exception("This exception wasn't even in the original class."));
	}
}
