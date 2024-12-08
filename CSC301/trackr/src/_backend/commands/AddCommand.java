package _backend.commands;

import _backend.framework.controllers.InputManager;
import _backend.utils.database.DBMS;
import _backend.utils.errorHandling.ErrorLogger;

// Think about having a superclass called DatabaseCommand that has a database property
// Ideas: keep an integer about the # of arguments for ????
// Ideas: Should I sanitize the primary key? Or should one have to parse it when coding

/**
 * Command that handles adding information to a database.
 *
 *
 */
public class AddCommand extends DatabaseCommand {
    private String primary_key = null; // The key for the database
    private InputManager inputManager;

    /**
     * Constructor for an AddCommand that specifies all information to add.
     *
     * @param database    -> name of database
     * @param primary_key -> key for database
     * @param value       -> information to store into the database
     */
    public AddCommand(String database, String primary_key, String[] value) {
        super(database);
        this.value = value;

        this.setPrimaryKey(primary_key);
    }

    /**
     * Constructor for an AddCommand that only specifies the database and what key
     * said database uses.
     *
     * @param database    -> name of database
     * @param primary_key -> key for database
     */
    public AddCommand(String database, String primary_key) {
        super(database);
        this.setPrimaryKey(primary_key);
    }

    /**
     * args[0] = database name - "UserAccount", "PublicList" args[1] = key/columns -
     * (username, password), (showname, episode, review), etc. args[2] = values -
     * ["faiz", "password"], ["digimon", "1", "It was great!"], etc.
     *
     * @param args
     */
    public AddCommand(Object[] args) {
        super((String) args[0]);

        if (args.length > 1) {
            if (!(args[1] instanceof String)) {
                throw new IllegalArgumentException();
            }

            this.setPrimaryKey((String) args[1]);
        }

        if (args.length > 2) {
            if (!(args[2] instanceof String[])) {
                throw new IllegalArgumentException();
            }

            this.value = (String[]) args[2];
        }
    }

    /**
     * Adds information into the database.
     */
    @Override
    public void execute() {
        checkForErrors();

        if (this.conn == null) {
            System.out.println("Cannot Connect to database, aborting");
            return;
        }

        String sqlQuery = "INSERT into " + this.database + " " + this.primary_key + " VALUES(" + this.questionMarks
                + ");";

        DBMS.updateDatabase(this.conn, sqlQuery, this.value);

        DBMS.closeConnection(this.conn);
    };

    /**
     * Sets the primary key to the specified key.
     *
     * Note to Self: Think about having it parsed correctly in the view, or doing
     * the parsing in this class. Probably this class.
     * 
     * @param name -> the primary key for the database
     */
    public void setPrimaryKey(String name) {
        this.primary_key = name;
        this.questionMarks = CommandUtils.generateQuestionmarks(this.primary_key, null);
    }

    /**
     * Private method that checks for errors before running the run() method.
     */
    private void checkForErrors() {
        boolean foundError = false;

        if (this.primary_key == null) {
            ErrorLogger.logMessage("Primary Key not set for AddCommand");
            foundError = true;
        }

        if (this.value == null) {
            ErrorLogger.logMessage("Value not set for AddCommand");
            foundError = true;
        }

        if (foundError)
            throw new IllegalArgumentException();
    }

    @Override
    public void setInputManager(InputManager im) {
        this.inputManager = im;
        this.inputManager.addCommand(this);
    }
}
