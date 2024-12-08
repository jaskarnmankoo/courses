package _backend.commands;

import java.sql.Connection;

import _backend.framework.controllers.InputManager;
import _backend.utils.database.DBMS;
import _backend.utils.errorHandling.ErrorLogger;

public class ModifyCommand extends DatabaseCommand {
    private String key = null; // key of the entry you want to modify by
    private String[] keys; // List of keys to modify
    private InputManager inputManager;

    /**
     * Constructor for ModifyCommand that specifies all information to modify.
     *
     * @param database     -> name of database
     * @param modify_entry -> key's entry name to modify
     * @param keys         -> keys you want to modify by
     * @param values       -> information to modify
     */
    public ModifyCommand(String database, String modify_entry, String[] keys, String[] values) {
        super(database);
        this.setKeys(keys);
        this.value = new String[values.length + 1];

        for (int i = 0; i < this.value.length - 1; i++) {
            this.value[i] = values[i];
        }

        this.value[this.value.length - 1] = modify_entry;
    }

    /**
     * Constructor for ModifyCommand that specifies only database, and keys to
     * modify along with the key to modify by.
     *
     * @param database    -> name of database
     * @param primary_key -> key you want to modify by
     * @param keys        -> keys to modify information about
     */
    public ModifyCommand(String database, String primary_key, String[] keys) {
        super(database);
        this.key = primary_key;
        this.setKeys(keys);
        this.value = new String[this.keys.length + 1];
    }

    public ModifyCommand(Object[] args) {
        super((String) args[0]);

        if (args.length > 1) {
            if (!(args[1] instanceof String)) {
                ErrorLogger.logMessage("Key to modify incorrect:" + (String) args[1]);
            }

            this.key = (String) args[1];
        }

        if (args.length > 2) {
            if (!(args[2] instanceof String[])) {
                ErrorLogger.logMessage("Keys modifying incorrect:" + (String[]) args[2]);
            }

            this.setKeys((String[]) args[2]);
        }

        if (args.length > 3) {
            if (!(args[3] instanceof String[])) {
                ErrorLogger.logMessage("Values incorrect:" + (String[]) args[3]);
            }
            this.value = (String[]) args[3];
        }
    }

    /**
     * Change information about an entry in the database
     */
    @Override
    public void execute() {
        checkForErrors();

        // Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase.db");

        String sqlQuery = "UPDATE PublicList set " + this.questionMarks + " WHERE " + this.key + "=?;";
        DBMS.updateDatabase(conn, sqlQuery, this.value);
        DBMS.closeConnection(conn);
    }

    /**
     * Sets the keys to delete by to this whilst also setting up the question marks.
     * 
     * @param keys -> keys to delete by
     */
    private void setKeys(String[] keys) {
        this.keys = keys;
        this.questionMarks = CommandUtils.generateModifyMarks(this.keys);
    }

    /**
     * Private method to put all error checking in.
     */
    private void checkForErrors() {
        boolean foundError = false;

        if (this.value == null) {
            ErrorLogger.logMessage("Value not set in ModifyCommand");
            foundError = true;
        }

        if (this.keys == null) {
            ErrorLogger.logMessage("Keys to modify by not set in ModifyCommand");
            foundError = true;
        }

        if (this.keys.length != this.value.length - 1) {
            ErrorLogger.logMessage("Modify parameters mismatched in ModifyCommand");
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
