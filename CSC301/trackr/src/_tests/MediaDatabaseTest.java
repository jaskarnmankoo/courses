package _tests;

import java.sql.Connection;

import _backend.utils.database.DBMS;

public class MediaDatabaseTest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void DBNew() {
		Connection conn = DBMS.establishServerDatabaseConnection("csc301");
		DBMS.updateDatabase(conn, "INSERT INTO GlobalMedia (mediatitle, mediatype) VALUES ('Birds', 'Movie')",
				new String[] {});
		DBMS.updateDatabase(conn, "INSERT INTO GlobalMedia (mediatitle, mediatype) VALUES ('BirdsTwo', 'Movie')",
				new String[] {});
		System.out.println("inserted");
		DBMS.closeConnection(conn);
	}

	public static void DBDel() {
		Connection conn = DBMS.establishServerDatabaseConnection("csc301");
		DBMS.updateDatabase(conn, "DELETE FROM GlobalMedia where mediatitle='Birds'", new String[] {});
		DBMS.updateDatabase(conn, "DELETE FROM GlobalMedia where mediatitle='BirdsTwo'", new String[] {});
		System.out.println("deleted");
		DBMS.closeConnection(conn);
	}

	public static void DBUpdate() {
		Connection conn = DBMS.establishServerDatabaseConnection("csc301");
		DBMS.updateDatabase(conn, "DROP TABLE IF EXISTS MediaTable;", new String[] {});
		DBMS.updateDatabase(conn,
				"CREATE TABLE IF NOT EXISTS MediaTable (id VARCHAR(50) PRIMARY KEY, title VARCHAR(50), producer VARCHAR(50), genre VARCHAR(50), contentRating VARCHAR(50), seasons INT, episodes INT, rating1 INT, rating2 INT, rating3 INT, rating4 INT, rating5 INT);",
				new String[] {});
		DBMS.closeConnection(conn);
	}
}
