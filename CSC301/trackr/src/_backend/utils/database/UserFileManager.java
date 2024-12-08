package _backend.utils.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import _backend.utils.errorHandling.ErrorLogger;

/**
 * This is a handler for the DBMS and its purpose is to deal with outside file
 * sources that are used with the database
 *
 */
public class UserFileManager {
	public static final int USER_DATABASE = 0, BASIC_SERVER_DATABASE = 1;

	/**
	 * Initializes the given database (through the open connection) with our schema.
	 *
	 * @param conn -> An open connection to the LOCAL database
	 */
	public static void loadLocalSchema(Connection conn, int type) {
		try {
			// Now reads from a schema and load the schema!
			System.out.println("Creating new database with our schema!");

			FileReader fileReader = null;

			if (type == BASIC_SERVER_DATABASE) { // Same schema as that of the server side database
				fileReader = new FileReader("resources/database/globalDataSchema.sql");
			} else if (type == USER_DATABASE) { // The schema that setups the public/private list of each user
				fileReader = new FileReader("resources/database/userSchema.sql");
			} else { // If neither is picked then error, but for now just exit without doing anything
				System.out.println("NOT AN OPTION");
				return;
			}

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String currentQuery = "";

			while ((currentQuery = bufferedReader.readLine()) != null) {
				DBMS.updateDatabase(conn, currentQuery, new String[] {});
			}

			if (fileReader != null)
				fileReader.close();
			if (bufferedReader != null)
				bufferedReader.close();

		} catch (Exception e) {
			ErrorLogger.logError(e);
		}
	}

	/**
	 * Syncs the Public and Private List on the local database with the one on the
	 * server.
	 *
	 * @param username -> User to sync public/private list with
	 */
	public static void pullUserDatabaseToLocal(String username) {
		Connection server = DBMS.establishServerDatabaseConnection(username.toLowerCase());

		if (server == null) {
			System.out.println("Could not connect to Server, aborting pull");
			return;
		}

		List<Map<String, Object>> resultsPublic = DBMS.queryDatabase(server, "SELECT * FROM PublicList",
				new String[] {});
		List<Map<String, Object>> resultsPrivate = DBMS.queryDatabase(server, "SELECT * FROM PrivateList",
				new String[] {});

		try {
			UserFileManager.initializeDatabase(System.getProperty("user.home"), username,
					UserFileManager.USER_DATABASE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Connection local = DBMS.establishLocalDatabaseConnection(username);

		String insertQuery = "INSERT INTO PublicList "
				+ "(mediatitle, mediatype, mediagenre, mediaduration, status, episodecount, startDate, endDate)  "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

		String insertQuery2 = "INSERT INTO PrivateList "
				+ "(mediatitle, mediatype, mediagenre, mediaduration, status, episodecount, startDate, endDate)  "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

		for (Map<String, Object> result : resultsPublic) {
			if (!DBMS.inDatabase(local, "SELECT * FROM PublicList WHERE mediatitle = ?",
					new String[] { (String) result.get("mediatitle") })) {
				String[] args = { (String) result.get("mediatitle"), (String) result.get("mediatype"),
						(String) result.get("mediagenre"), (String) result.get("mediaduration"),
						(String) result.get("status"), (String) result.get("episodecount"),
						(String) result.get("startDate"), (String) result.get("endDate") };
				DBMS.updateDatabase(local, insertQuery, args);
			}
		}

		for (Map<String, Object> result : resultsPrivate) {
			if (!DBMS.inDatabase(local, "SELECT * FROM PrivateList WHERE mediatitle = ?",
					new String[] { (String) result.get("mediatitle") })) {
				String[] args = { (String) result.get("mediatitle"), (String) result.get("mediatype"),
						(String) result.get("mediagenre"), (String) result.get("mediaduration"),
						(String) result.get("status"), (String) result.get("episodecount"),
						(String) result.get("startDate"), (String) result.get("endDate") };
				DBMS.updateDatabase(local, insertQuery2, args);
			}
		}

		DBMS.closeConnection(local);
		DBMS.closeConnection(server);
	}

	/**
	 * Syncs the server's database of the user with the local database's version of
	 * the user.
	 *
	 * @param conn     -> An open connection to a database
	 * @param username -> the user currently operating the app
	 */
	public static void pushUserDatabaseToServer(String username) {
		// Connect to the new user database in the server and load the user schema
		Connection server = DBMS.establishServerDatabaseConnection(username.toLowerCase());
		if (server == null) {
			System.out.println("Failed to push to User database to server");
			DBMS.closeConnection(server);
			return;
		}

		UserFileManager.loadLocalSchema(server, UserFileManager.USER_DATABASE);

		// Load it with information we have stored for the user
		Connection local = DBMS.establishLocalDatabaseConnection(username);

		String query = "INSERT INTO PublicList (mediatitle, mediatype, mediagenre, mediaduration, status, episodecount, startDate, endDate)  VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

		List<Map<String, Object>> results = DBMS.queryDatabase(local, "SELECT * FROM PublicList", new String[] {});

		// Sync the Users Public List to the server
		for (Map<String, Object> result : results) {
			if (!DBMS.inDatabase(server, "SELECT * FROM PublicList WHERE mediatitle = ?",
					new String[] { (String) result.get("mediatitle") })) {
				String[] args = { (String) result.get("mediatitle"), (String) result.get("mediatype"),
						(String) result.get("mediagenre"), (String) result.get("mediaduration"),
						(String) result.get("status"), (String) result.get("episodecount"),
						(String) result.get("startDate"), (String) result.get("endDate") };
				DBMS.updateDatabase(server, query, args);
			}
		}

		query = "INSERT INTO PrivateList(mediatitle, mediatype, mediagenre, mediaduration, status, episodecount, startDate, endDate)  VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		results = DBMS.queryDatabase(local, "SELECT * FROM PrivateList", new String[] {});

		// Sync the Users Private List to the server
		for (Map<String, Object> result : results) {
			if (!DBMS.inDatabase(server, "SELECT * FROM PrivateList WHERE mediatitle = ?",
					new String[] { (String) result.get("mediatitle") })) {
				String[] args = { (String) result.get("mediatitle"), (String) result.get("mediatype"),
						(String) result.get("mediagenre"), (String) result.get("mediaduration"),
						(String) result.get("status"), (String) result.get("episodecount"),
						(String) result.get("startDate"), (String) result.get("endDate") };
				DBMS.updateDatabase(server, query, args);
			}
		}

		DBMS.closeConnection(local);
		DBMS.closeConnection(server);
	}

	/**
	 * Sets up the directory named IlistDemedia
	 *
	 * @param baseFolder -> The folder which all this will be held in
	 */
	private static void initializeFolder(String baseFolder) {
		Path folderPath = Paths.get(String.format("%s/IlistDemedia", baseFolder));
		File folder = folderPath.toFile();
		if (!folder.exists()) { // Checks for the existence of the folder
			folder.mkdir();
		}
	}

	/**
	 * Initializes a new database on the user's local machine.
	 *
	 * @param baseFolder   -> Name of the folder the IlistDemedia folder is in
	 * @param databaseName -> Name of the Database to create
	 * @param type         -> The type of database being created for the user (Refer
	 *                     to UserFileManager Constants)
	 * @throws IOException
	 */
	public static void initializeDatabase(String baseFolder, String databaseName, int type) throws IOException {
		Path databaseFilePath = Paths.get(String.format("%s/IlistDemedia/%s.db", baseFolder, databaseName));
		File databaseFile = databaseFilePath.toFile();
		if (!databaseFile.exists()) { // If the file doesn't exist make it
			databaseFile.createNewFile();
			System.out.println(String.format("%s/IlistDemedia/%s.db", baseFolder, databaseName));

			// Load the Schema into the new Database

			Connection conn = DBMS.establishLocalDatabaseConnection(databaseName);
			UserFileManager.loadLocalSchema(conn, type);
			DBMS.closeConnection(conn);

		}
	}

	/**
	 * Used at startup to confirm that the user has all needed files for the DBMS to
	 * function in the way it should. If the user doesn't have said files then we
	 * will add them into their home directory.
	 *
	 * Note: Need to handle edge cases for the file and directory creation -What if
	 * the path exists but isn't a directory??
	 */
	public static void initializeUserFiles() {
		try {
			// Lets get the native home directory
			String homeDir = System.getProperty("user.home");

			// Check and make the folder if we need to
			UserFileManager.initializeFolder(homeDir);

			// Check if the user needs a replica of the server's database
			UserFileManager.initializeDatabase(homeDir, "TrackrDatabase", UserFileManager.BASIC_SERVER_DATABASE);

		} catch (IOException e) {
			ErrorLogger.logError(e);
		}
	}

	/**
	 * Local database is sync'd with the server database.
	 *
	 */
	public static void syncWithServer() {
		Connection local = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
		Connection server = DBMS.establishServerDatabaseConnection("csc301");

		if (server == null || local == null) {
			System.out.println("Failed to sync. Databases may not be setup properly");
			DBMS.closeConnection(server);
			DBMS.closeConnection(local);
			return;
		}

		String serverQuery = "SELECT * FROM UserAccount";
		String localQuery = "INSERT INTO UserAccount (username, password, email, firstname, lastname)  VALUES(?, ?, ?, ?, ?)";

		List<Map<String, Object>> results = DBMS.queryDatabase(server, serverQuery, new String[] {});

		// Sync the Users
		for (Map<String, Object> result : results) {
			if (!DBMS.inDatabase(local, "SELECT * FROM UserAccount WHERE username = ?",
					new String[] { (String) result.get("username") })) {

				String[] args = { (String) result.get("username"), (String) result.get("password"),
						(String) result.get("email"), (String) result.get("firstname"),
						(String) result.get("lastname") };
				DBMS.updateDatabase(local, localQuery, args);
			}
		}

		// Sync the GlobalMedia
		serverQuery = "SELECT * FROM GlobalMedia";
		localQuery = "INSERT INTO GlobalMedia (mediatitle, mediatype, mediagenre, mediaduration)  VALUES(?, ?, ?, ?)";

		results = DBMS.queryDatabase(server, serverQuery, new String[] {});

		for (Map<String, Object> result : results) {
			if (!DBMS.inDatabase(local, "SELECT * FROM GlobalMedia WHERE mediatitle = ?",
					new String[] { (String) result.get("mediatitle") })) {

				String[] args = { (String) result.get("mediatitle"), (String) result.get("mediatype"),
						(String) result.get("mediagenre"), (String) result.get("mediaduration") };
				DBMS.updateDatabase(local, localQuery, args);
			}
		}

		// Sync the Ratings
		serverQuery = "SELECT * FROM Ratings";
		localQuery = "INSERT INTO Ratings (username, mediatitle, review, rating)  VALUES(?, ?, ?, ?)";

		results = DBMS.queryDatabase(server, serverQuery, new String[] {});
		// System.out.println(results);

		for (Map<String, Object> result : results) {
			if (!DBMS.inDatabase(local, "SELECT * FROM Ratings WHERE mediatitle = ?",
					new String[] { (String) result.get("mediatitle") })) {
				String[] args = { (String) result.get("username"), (String) result.get("mediatitle"),
						(String) result.get("review"), (String) result.get("rating") };
				DBMS.updateDatabase(local, localQuery, args);
			}
		}

		// Done syncing so close both connections
		DBMS.closeConnection(server);
		DBMS.closeConnection(local);
	}
}
