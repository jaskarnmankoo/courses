package _backend.utils.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import _backend.framework.models.media.GMedia;
import _backend.framework.models.media.PMedia;
import _backend.utils.applicationExceptions.AddMediaException;
import _backend.utils.errorHandling.ErrorLogger;

/**
 * Class that contains a specialized HashMap for all our Media. Performs certain
 * actions accordingly.
 *
 */
public class MediaHashMap {
	// Constants
	public final static String PUBLIC = "PublicList", PRIVATE = "PrivateList", GLOBAL = "GlobalMedia";
	public final static String MOVIE = "Movie", TV_SHOW = "TV Show";
	public final static String SERVER_DB = "csc301";
	public final static String LOCAL_DB = "TrackrDatabase";

	private HashMap<String, PMedia> mediaHash; // HashMap of a user's personal media
	protected String table; // Table
	protected String database; // What database the information lives on

	/**
	 * Creates a MediaHashMap based on the given table and database.
	 *
	 * @param table
	 * @param database
	 */
	public MediaHashMap(String table, String database) {
		try {
			this.table = table;

			if (table.equals(GLOBAL) && !database.equals(LOCAL_DB)) {
				throw new Exception("Database being used for the GlobalMedia is not TrackrDatabase");
			}

			this.table = table;
			this.database = database;
			this.mediaHash = new HashMap<String, PMedia>();

			// Create the connection
			System.out.println("Trying to connect to " + this.database);
			Connection localConn = DBMS.establishLocalDatabaseConnection(this.database);
			String preparedSQL = "SELECT * FROM " + this.table;
			String[] args = {};

			List<Map<String, Object>> results = DBMS.queryDatabase(localConn, preparedSQL, args);

			for (Map<String, Object> mediaItem : results) {
				PMedia media = new PMedia(mediaItem);
				this.add(media);
			}

			DBMS.closeConnection(localConn);
		} catch (Exception e) {
			ErrorLogger.logError(e);
		}
	}

	/**
	 * Adds a Media Object into the MediaHashMap.
	 *
	 * @param media
	 */
	public void add(GMedia media) {
		try {
			if (this.mediaHash.containsKey(media.getTitle())) {
				throw new AddMediaException("");
			}

			PMedia pMedia = (PMedia) media;

			// Add media into Hash
			this.mediaHash.put(media.getTitle(), pMedia);

			String insertQuery = "INSERT INTO " + this.table
					+ " (mediatitle, mediatype, mediagenre, mediaduration, status, episodecount, startdate, enddate) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			String args[] = { pMedia.getTitle(), pMedia.getType(), pMedia.getGenre(),
					Integer.toString(pMedia.getDuration()), pMedia.getStatus(),
					Integer.toString(pMedia.getEpisodeCount()), pMedia.getStartDate(), pMedia.getEndDate() };

			// Create a local connection to SQLite
			Connection localConn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");

			if (localConn == null)
				throw new Exception("Local connection failed.");

			boolean mediaFoundLocally = DBMS.inDatabase(localConn, "SELECT * FROM GlobalMedia WHERE mediatitle = ?",
					new String[] { media.getTitle() });
			if (!mediaFoundLocally) {
				DBMS.updateDatabase(localConn, insertQuery, args);
			}
			DBMS.closeConnection(localConn);

			// Create a server connection to PostGreSQL
			Connection serverConn = DBMS.establishServerDatabaseConnection(SERVER_DB);
			if (serverConn == null) {
				System.out.println("Cannot connect to server. Exiting...");
				return;
			}

			boolean mediaFoundServer = DBMS.inDatabase(serverConn, "SELECT * FROM GlobalMedia WHERE mediatitle = ?",
					new String[] { media.getTitle() });
			if (!mediaFoundServer) {
				DBMS.updateDatabase(serverConn, insertQuery, args);
			}

			DBMS.closeConnection(serverConn);
		} catch (Exception e) {
			ErrorLogger.logError(e);
		}
	}

	/**
	 * Removes a Media Object from the HashMap.
	 *
	 * @param mediaKey
	 */
	public void remove(String mediaKey) {
		try {
			if (!this.mediaHash.containsKey(mediaKey))
				throw new Exception("Removal failed. Item does not exist in the local database.");

			this.mediaHash.remove(mediaKey);

			String deleteQuery = "DELETE FROM " + this.table + " WHERE mediatitle = ?";

			String args[] = { mediaKey };

			// Connect locally to SQLite to delete the item
			Connection localConn = DBMS.establishLocalDatabaseConnection(this.database);

			if (localConn == null)
				throw new Exception("Local connection failed");

			DBMS.updateDatabase(localConn, deleteQuery, args);
			DBMS.closeConnection(localConn);

			// Connect server to PostGreSQL to delete the item
			Connection serverConn = DBMS.establishServerDatabaseConnection(SERVER_DB);

			if (serverConn == null) {
				System.out.println("Cannot connect to server. Exiting...");
				return;
			}

			DBMS.updateDatabase(serverConn, deleteQuery, args);
			DBMS.closeConnection(serverConn);

		} catch (Exception e) {
			ErrorLogger.logError(e);
		}
	}

	/**
	 * Retrieve media from local database
	 * <p>
	 * The media can be grabbed from the HashMap when called and retrieved by the
	 * user.
	 * <p>
	 * 
	 * @param key
	 * @return An accessible Media item that can be typecasted when overloaded with
	 *         the properly semantic method (getMovie or getShow)
	 */
	public PMedia getMedia(String key) {
		System.out.println(this.mediaHash);
		try {
			if (!this.mediaHash.containsKey(key)) {
				throw new Exception("Item does not exist in the HashMap");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return this.mediaHash.get(key);
	}

	/**
	 * Searches through the MediaHashMap using a key. If key is in HashMap, return
	 * the list of it. Else if key is nowhere in the MediaHashMap, return an empty
	 * ArrayList.
	 *
	 * @param queryKey
	 * @return
	 */
	public Map<String, GMedia> search(String queryKey) {
		List<String> queryList = new ArrayList<>();
		queryList.add(queryKey);

		Map<String, GMedia> tempHash = queryList.stream().filter(this.mediaHash::containsKey)
				.collect(Collectors.toMap(Function.identity(), this.mediaHash::get));
		return tempHash;
	}

	/**
	 * Returns the entire HashMap
	 *
	 * @return
	 */
	public HashMap<String, PMedia> getHashMap() {
		return this.mediaHash;
	}

	/**
	 * Update/Modify an entry in the MediaHashMap.
	 *
	 * @param changedMedia
	 */
	public void updateMedia(GMedia changedMedia) {
		try {
			if (!this.mediaHash.containsKey(changedMedia.getTitle())) {
				throw new Exception("Media does not exist in the hash.");
			}

			this.mediaHash.put(changedMedia.getTitle(), (PMedia) changedMedia);
			PMedia pMedia = (PMedia) changedMedia;
			String updateQuery = "UPDATE " + this.table + " SET status = ?, startdate = ?, enddate = ?";

			String args[] = { pMedia.getStatus(), pMedia.getStartDate(), pMedia.getEndDate() };

			Connection localConn = DBMS.establishLocalDatabaseConnection(this.database);

			if (localConn == null)
				throw new Exception("Local connection failed");

			DBMS.updateDatabase(localConn, updateQuery, args);
			DBMS.closeConnection(localConn);

			Connection serverConn = DBMS.establishServerDatabaseConnection(SERVER_DB);
			if (serverConn == null) {
				System.out.println("Cannot connect to server. Exiting...");
				return;
			}
			DBMS.updateDatabase(serverConn, updateQuery, args);
			DBMS.closeConnection(serverConn);
		} catch (Exception e) {
			ErrorLogger.logError(e);
		}
	}
}
