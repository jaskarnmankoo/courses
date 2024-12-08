package _backend.utils.database;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import _backend.framework.models.media.GMedia;
import _backend.utils.applicationExceptions.AddMediaException;
import _backend.utils.errorHandling.ErrorLogger;

public class GlobalHashMap extends MediaHashMap {
	private HashMap<String, GMedia> globalHash; // HashMap containing all the media on the server database.

	public GlobalHashMap(String table, String database) {
		super(table, "TrackrDatabase");

		try {
			if (!(table.equals(PUBLIC) || table.equals(PRIVATE) || table.equals(GLOBAL))) {
				ErrorLogger.logMessage("Table must be PublicList or PrivateList or Global");
			} else {
				this.table = table;
				this.database = database;
				this.globalHash = new HashMap<String, GMedia>();

				// Create the connection
				Connection localConn = DBMS.establishLocalDatabaseConnection(this.database);
				String preparedSQL = "SELECT * FROM " + this.table;
				String[] args = {};

				List<Map<String, Object>> results = DBMS.queryDatabase(localConn, preparedSQL, args);

				for (Map<String, Object> mediaItem : results) {
					GMedia media = new GMedia(mediaItem);
					this.add(media);
				}

				DBMS.closeConnection(localConn);
			}
		} catch (Exception e) {
			ErrorLogger.logError(e);
		}
	}

	@Override
	public void add(GMedia media) {
		try {
			if (this.globalHash.containsKey(media.getTitle())) {
				throw new AddMediaException("");
			}

			// Add media into Hash
			this.globalHash.put(media.getTitle(), media);

			String insertQuery = "INSERT INTO " + this.table
					+ " (mediatitle, mediatype, mediagenre, mediaduration) VALUES(?, ?, ?, ?)";

			String args[] = { media.getTitle(), media.getType(), media.getGenre(),
					Integer.toString(media.getDuration()) };

			// Create a local connection to SQLite
			Connection localConn = DBMS.establishLocalDatabaseConnection(this.database);

			if (localConn == null)
				throw new Exception("Local connection failed.");

			boolean mediaFoundLocal = DBMS.inDatabase(localConn, "SELECT * FROM GlobalMedia WHERE mediatitle = ?",
					new String[] { media.getTitle() });
			if (!mediaFoundLocal) {
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
}
