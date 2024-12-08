package _backend.commands;

import java.sql.Connection;

import _backend.framework.controllers.InputManager;
import _backend.utils.database.DBMS;
import _backend.utils.errorHandling.ErrorLogger;

public class RemoveCommand extends DatabaseCommand {
    private String[] delete_keys = null; // Keys to delete by for the database
    private InputManager inputManager;

    /**
     * Constructor for a RemoveCommand. All information needed for a remove is
     * specified for this.
     *
     * @param database -> name of database
     * @param keys     -> keys to delete by
     * @param value    -> value to delete by
     */
    public RemoveCommand(String database, String[] keys, String[] value) {
        super(database);
        this.setKeys(keys);
        this.value = value;
    }

    /**
     * Constructor for a RemoveCommand. This one only specifies the database and
     * keys to delete by.
     *
     * @param database -> name of database
     * @param keys     -> keys to delete by
     */
    public RemoveCommand(String database, String[] keys) {
        super(database);
        this.setKeys(keys);
    }

    /**
     * args[0] = database name args[1] = keys/columns to delete by, ["showname",
     * "episode"] args[2] = values to delete by, ["Katekyo", "3"]
     *
     * @param args
     */
    public RemoveCommand(Object[] args) {
        super((String) args[0]);

        if (args.length > 1) {
            if (!(args[1] instanceof String[])) {
                throw new IllegalArgumentException();
            }

            this.setKeys((String[]) args[1]);
        }

        if (args.length > 2) {
            if (!(args[2] instanceof String[])) {
                throw new IllegalArgumentException();
            }

            this.value = (String[]) args[2];
        }
    }

    /**
     * Deletes an entry in the database.
     */
    @Override
    public void execute() {
        checkForErrors();
        Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase.db");
        DBMS.updateDatabase(conn, "DELETE FROM " + this.database + " WHERE " + this.questionMarks + ";", this.value);
        DBMS.closeConnection(conn);
    }

    /**
     * Sets the keys to delete by to this whilst also setting up the question marks.
     * 
     * @param keys -> keys to delete by
     */
    private void setKeys(String[] keys) {
        this.delete_keys = keys;
        this.questionMarks = CommandUtils.generateQuestionmarks(null, this.delete_keys);
    }

    /**
     * Private method to put all error checking in.
     */
    private void checkForErrors() {
        boolean foundError = false;

        if (this.delete_keys == null) {
            ErrorLogger.logMessage("Primary Key not set for RemoveCommand");
            foundError = true;
        }

        if (this.value == null) {
            ErrorLogger.logMessage("Value not set for RemoveCommand");
            foundError = true;
        }

        if (this.delete_keys.length != this.value.length) {
            ErrorLogger.logMessage("Delete parameters are mismatched in RemoveCommand.");
            foundError = true;
        }

        if (foundError)
            throw new IllegalArgumentException();
    }

    @Override
    public void setInputManager(InputManager im) {
        this.inputManager = im;
    }
}
