package _backend.framework.controllers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import _backend.commands.ICommand;
import _backend.utils.database.DBMS;
import _backend.utils.factories.CommandFactory;
import _backend.utils.factories.InputListenerFactory;
import _backend.utils.factories.StateFactory;

/**
 * Manages all Input the User performs on the Trackr Application. This allows
 * for a centralization of Input and we won't need to look through a bunch of
 * different classes.
 *
 * Note: Should only be communicating with ActionListeners, other classes
 * besides the listeners should not be calling it.
 *
 * This should have a list of all ActionListeners to it. When it receives a
 * trigger run execute?
 *
 */
public class InputManager {
    private static InputManager instance = null; // Singleton
    public static final InputListenerFactory INPUT_LISTENER_FACTORY = new InputListenerFactory(
            InputManager.getInstance());
    public static final CommandFactory COMMAND_FACTORY = new CommandFactory(InputManager.getInstance());
    private List<ICommand> commands = new ArrayList<ICommand>();

    private InputManager() {
    }

    /**
     * Executes a given set of commands.
     * 
     * @param commands
     */

    public void executeCommands(List<ICommand> commands) {
        for (ICommand command : commands) {
            command.execute();
        }
    }

    public void executeCommands(ICommand[] commands) {
        for (ICommand command : commands) {
            if (command != null) {
                command.execute();
            }
        }
    }

    public void executeCommands() {
        for (ICommand command : this.commands)
            command.execute();
        this.commands.clear();
    }

    /**
     * Returns the InputManager instance.
     *
     * There will always be only one instance of the InputManager at any given time.
     *
     * @return The InputManager instance. If instance is not initialized
     *         (instance=null), create the instance.
     */
    static InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }

        return instance;
    }

    /**
     * Pokes the StateManager to switch the currentState to a new State.
     *
     * @param state -> A ViewState to switch to
     */
    public void switchTo(StateFactory.State state) {
        StateManager.getInstance().swapState(state);
    }

    public void modifyData(String name, String ep) {
        Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase.db");
        DBMS.updateDatabase(conn, "UPDATE PublicList set episode=? WHERE showname=?;", new String[] { ep, name });
        DBMS.closeConnection(conn);
    }

    public void addCommand(ICommand command) {
        this.commands.add(command);

    }
}
