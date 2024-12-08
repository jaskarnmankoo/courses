package _backend.inputListeners;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

import _backend.commands.AddCommand;
import _backend.commands.ICommand;
import _backend.framework.controllers.InputManager;
import _backend.framework.view.ViewState;

/**
 * This specific listener deals with taking user input about a specific media
 *
 */
public class AddMediaListener extends AbstractListener {
    private JTextField nameTextField, episodeTextField;
    private ViewState state;

    /**
     * Constructor for an AddMediaListener. Takes in an InputManager and a list of
     * Object[] args which are:
     *
     * args[0] = ViewState - the current view args[1] = JTextField - name of media
     * to add args[2] = JTextField - episode count to add args[3] = String - name of
     * database args[4] = String - primary key to use
     *
     * @param inputManager
     * @param args
     */
    public AddMediaListener(InputManager inputManager, Object[] args) {
        super(inputManager, args);
        this.state = (ViewState) args[0];
        this.nameTextField = (JTextField) args[1];
        this.episodeTextField = (JTextField) args[2];

        // List of commands to run should be set here
        // args[3] must be a String and be the database to add to,
        // args[4] is a string that is the primary_key
        this.commands.add(new AddCommand((String) args[3], (String) args[4]));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Create/set the commands here, send them over to InputManager to be executed.
        // list of commands - protected ICommand
        // set certain things about commands in this function.
        // run the execute function in InputManager.

        System.out.println("Adding Media Now");
        String showName = this.nameTextField.getText(), episode = this.episodeTextField.getText();

        ((AddCommand) this.commands.get(0)).setValue(new String[] { showName, episode });

        this.inputManager.executeCommands(this.commands);
        this.state.updateAspects();
    }
}
