package _backend.inputListeners;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.Arrays;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import _backend.commands.AddCommand;
import _backend.commands.ICommand;
import _backend.commands.SwapCommand;
import _backend.framework.controllers.InputManager;
import _backend.utils.database.DBMS;
import _backend.utils.database.UserFileManager;
import _backend.utils.factories.CommandFactory;
import _backend.utils.factories.StateFactory;

/**
 * This is a specific case of the AbstractListener which adds the given user
 * information into the database.
 *
 * Also checks if given a state to swap states with.
 *
 */
public class AddUserListener extends AbstractListener {
    private JTextField name, firstName, lastName, email;
    private JPasswordField password;
    private AddCommand addCommand = null;
    private SwapCommand swapCommand = null;

    /**
     * Constructor of AddUserListener. List of Object[] args:
     *
     * args[2] = JTextField - name of user args[3] = JPasswordField - password of
     * user args[0] = String - name of database args[1] = String - primary key
     *
     * Note to self: keep a list of database names.
     *
     * @param inputManager
     * @param args
     */
    public AddUserListener(InputManager inputManager, Object[] args) {
        super(inputManager, args);

        // Safe cast since this listener is expecting a name and a password
        this.name = (JTextField) this.args[2];
        this.password = (JPasswordField) this.args[3];
        this.firstName = (JTextField) this.args[4];
        this.lastName = (JTextField) this.args[5];
        this.email = (JTextField) this.args[6];

        if (!(args[0] instanceof String)) {
            throw new IllegalArgumentException();
        }

        addCommand = (AddCommand) InputManager.COMMAND_FACTORY.getCommand(CommandFactory.CommandAction.ADD_USER,
                Arrays.copyOfRange(args, 0, 2));
        addCommand.setConnection((Connection) this.args[7]); // Setup the connection it needs

        if (args[args.length - 1] instanceof StateFactory.State) { // Check if there is a 4th argument, if there is,
                                                                   // also add swapping to a state
            swapCommand = (SwapCommand) InputManager.COMMAND_FACTORY.getCommand(CommandFactory.CommandAction.SWAP_STATE,
                    new Object[] { args[args.length - 1] });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        doLogic();
        InputManager.COMMAND_FACTORY.executeCommands(new ICommand[] { addCommand, swapCommand });
        try {
            // User has now registered so add his/her username.db to their computer and then
            // make it on the postgres server as well
            UserFileManager.initializeDatabase(System.getProperty("user.home"), this.name.getText(),
                    UserFileManager.USER_DATABASE);
            Connection conn = DBMS.establishServerDatabaseConnection("");
            String createDB = "CREATE DATABASE " + this.name.getText();
            DBMS.updateDatabase(conn, createDB, new String[] {});
            DBMS.closeConnection(conn);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void doLogic() {
        System.out.println("Adding User:" + this.name.getText() + " with password:"
                + new String(this.password.getPassword()) + " Now.");
        addCommand.setValue(new String[] { this.name.getText(), new String(this.password.getPassword()),
                this.firstName.getText(), this.lastName.getText(), this.email.getText() });
    }
}
