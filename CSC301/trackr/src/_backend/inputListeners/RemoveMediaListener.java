package _backend.inputListeners;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextField;

import _backend.commands.ICommand;
import _backend.commands.RemoveCommand;
import _backend.framework.controllers.InputManager;
import _backend.framework.view.ViewState;
import _backend.utils.factories.CommandFactory;

public class RemoveMediaListener extends AbstractListener {
    private JTextField nameTextField, episodeTextField;
    private ViewState state;
    private RemoveCommand removeCommand = null;

    /**
     * Constructor for RemoveMediaListener
     *
     * args[0] = ViewState - current state args[3] = JTextField - name of show to
     * remove args[4] = JTextField - episode of show to remove args[1] = String -
     * name of database args[2] = String[] - list of keys/columns in the database
     *
     * @param inputManager
     * @param args
     */
    public RemoveMediaListener(InputManager inputManager, Object[] args) {
        super(inputManager, args);
        this.state = (ViewState) args[0];
        this.nameTextField = (JTextField) args[3];
        this.episodeTextField = (JTextField) args[4];
        this.removeCommand = (RemoveCommand) InputManager.COMMAND_FACTORY
                .getCommand(CommandFactory.CommandAction.REMOVE_MEDIA, Arrays.copyOfRange(args, 1, 3));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        doLogic();

        InputManager.COMMAND_FACTORY.executeCommands(new ICommand[] { removeCommand });

        this.state.updateAspects();
    }

    private void doLogic() {
        System.out.println("Removing Media Now");
        String showName = this.nameTextField.getText(), episode = this.episodeTextField.getText();

        removeCommand.setValue(new String[] { showName, episode });
    }
}
