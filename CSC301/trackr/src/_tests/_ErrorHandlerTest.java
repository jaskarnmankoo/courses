package _tests;

import java.sql.SQLException;

import _backend.utils.errorHandling.ErrorLogger;

public class _ErrorHandlerTest {
	// Notice that when main calls a method "f", the logger will record method "f"
	// as the cause for the exception
	// If the exception is directly in main, the logger will record "main" as the
	// cause for the exception

	// If it calls c.f, where "c" is a different class, it will record class "c" as
	// the cause, also with its method "f"
	public static void main(String[] args) {
		ErrorLogger.logError(new Exception());
		ErrorLogger.logError(new Exception("Any exception messages you put will be recorded as well."));
		ErrorLogger.logError(new Exception("These 3 exceptions were run directly from main."));
		logIllegal();
		logSQL();

		_ErrorHandlerTest2.logFromHere();
	}

	private static void logIllegal() {
		ErrorLogger.logError(new IllegalArgumentException("This exception was run from a different method from main."));
	}

	private static void logSQL() {
		ErrorLogger.logError(new SQLException("So was this."));
		logMath();
	}

	private static void logMath() {
		ErrorLogger.logError(new ArithmeticException("This function was called in logSQL."));
	}
}
