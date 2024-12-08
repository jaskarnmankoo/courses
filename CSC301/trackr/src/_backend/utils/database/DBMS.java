package _backend.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import _backend.utils.errorHandling.ErrorLogger;

/**
 * This is our Database Management System. It allows the application to be
 * completely separate from the data it is dealing with and allows proper
 * storage of user data and media data.
 *
 */
public class DBMS {
	/**
	 * Converts the information of a ResultSet in a very simple and most commonly
	 * accepted way to locally store the information.
	 *
	 * Usage: list.get(rowNum).get(colName)
	 *
	 * @param results -> The ResultSet of a database query
	 * @return A nested data structure which represents the results given by the
	 *         ResultSet
	 */
	private static List<Map<String, Object>> convertResultSetToList(ResultSet results) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			ResultSetMetaData metaData = results.getMetaData();
			int columns = metaData.getColumnCount();

			while (results.next()) {
				HashMap<String, Object> row = new HashMap<String, Object>(columns);

				for (int col = 1; col <= columns; ++col) {
					row.put(metaData.getColumnName(col), results.getObject(col));
				}

				list.add(row);
			}
		} catch (Exception e) {
			ErrorLogger.logError(e);
		}

		return list;
	}

	/**
	 * Attempts to connect to the given database. Returns a Connection on success.
	 *
	 * @param database -> The name of the database that wants to be connected to
	 * @return An open database connection to the given local database
	 */
	public static Connection establishLocalDatabaseConnection(String database) {
		Connection conn = null;

		try {
			// Where does the database exist? Well it exists in the users home directory
			String userHome = System.getProperty("user.home");
			String databasePath = String.format("%s/IlistDemedia/%s.db", userHome, database);

			// db parameters
			String url = "jdbc:sqlite:" + databasePath;

			// create a connection to the database
			conn = DriverManager.getConnection(url);

			System.out.println("Connected");

		} catch (Exception e) {
			ErrorLogger.logError(e);
		}

		return conn;
	}

	/**
	 * Attempts to connect to the server database hosted on a Postgres server.
	 * Returns a Connection on success.
	 *
	 * @return An open database connection to the given local database
	 */
	public static Connection establishServerDatabaseConnection(String databaseName) {
		Connection conn = null;

		try {

			String url = "jdbc:postgresql://142.1.44.162/" + databaseName;
			Properties props = new Properties();
			props.setProperty("user", "postgres");
			props.setProperty("password", "kanjicrewpassword");

			conn = DriverManager.getConnection(url, props);

			System.out.println("Connected");

		} catch (Exception e) {
			ErrorLogger.logError(e);
		}

		return conn;
	}

	/**
	 * Takes the given connection and closes it if it's open.
	 *
	 * @param conn -> An open database connection
	 */
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
				System.out.println("Connection Closed");
			}
		} catch (Exception e) {
			ErrorLogger.logError(e);
		}
	}

	/**
	 * Takes the prepared SQL query and the given arguments for it and executes it.
	 * This function specifically deals with updating statements.
	 *
	 * @param conn        -> An open database connection
	 * @param preparedSQL -> A prepared SQL query
	 * @param args        -> Arguments for the given prepared SQL query
	 */
	public static void updateDatabase(Connection conn, String preparedSQL, String args[]) {
		try {

			PreparedStatement stmt = conn.prepareStatement(preparedSQL);
			for (int argNum = 0; argNum < args.length; argNum++) {
				stmt.setString(argNum + 1, args[argNum]);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (Exception e) {
			ErrorLogger.logError(e);
		}
	}

	/**
	 * Takes a preparedSQL query, and executes it with the given arguments. It
	 * returns the results in a nested List<Map> form.
	 *
	 * @param conn        -> An open database connection
	 * @param preparedSQL -> A prepared SQL query
	 * @param args        -> Arguments for the given prepared SQL query
	 * @return The results of the given query
	 */
	public static List<Map<String, Object>> queryDatabase(Connection conn, String preparedSQL, String args[]) {
		List<Map<String, Object>> information = null;

		try {

			PreparedStatement stmt = conn.prepareStatement(preparedSQL);
			for (int argNum = 0; argNum < args.length; argNum++) {
				stmt.setString(argNum + 1, args[argNum]);
			}

			ResultSet results = stmt.executeQuery();
			information = DBMS.convertResultSetToList(results);

			stmt.close();

		} catch (Exception e) {
			ErrorLogger.logError(e);
		}

		return information;
	}

	/**
	 *
	 * Checks if a *preparedQuery* is in the database the connection is referring
	 * to.
	 *
	 * @param conn          -> An open database connection
	 * @param preparedQuery -> A prepared SQL query
	 * @param args          -> Arguments for the given query
	 * @return True | False if the query is in the database
	 */
	public static boolean inDatabase(Connection conn, String preparedQuery, String[] args) {
		boolean inDatabase = false;

		// Show case of arguments for preparedStatement
		List<Map<String, Object>> results = DBMS.queryDatabase(conn, preparedQuery, args);

		if (results != null && results.size() != 0) {
			inDatabase = true;
		}

		return inDatabase;
	}
}
