package _backend.commands;

import java.sql.Connection;

/**
 * Abstract class that holds all relevant/shared information and methods for
 * Database-related commands.
 *
 */
public abstract class DatabaseCommand implements ICommand {
    protected String database; // name of database
    protected String questionMarks; // formatting of ? in SQL PreparedStatements
    protected Connection conn;
    protected String[] value = null; // information to insert into the database

    /**
     * Constructor to store which database the command should take effect on.
     *
     * @param database -> name of database
     */
    public DatabaseCommand(String database) {
        this.database = database;
    };

    /**
     * Stores the specific information you want to store into a database. Values are
     * specific information about a show. ie.<ShowName><EpisodeCount><Username> etc.
     *
     * @param val -> String array containing specific information.
     */
    public void setValue(String[] val) {
        this.value = val;
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }
}
