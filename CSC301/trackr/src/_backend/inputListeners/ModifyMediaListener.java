package _backend.inputListeners;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextField;

import _backend.commands.ICommand;
import _backend.commands.ModifyCommand;
import _backend.framework.controllers.InputManager;
import _backend.framework.view.ViewState;
import _backend.utils.factories.CommandFactory;

public class ModifyMediaListener extends AbstractListener {
    private JTextField nameTextField, episodeTextField;
    private ViewState state;
    private ModifyCommand modifyCommand = null;

    /**
     * Constructor for a ModifyMediaListener. A list of Object[] args:
     *
     * args[0] = ViewState - current state one is in args[1] = JTextField - name of
     * the show you want to modify args[2] = JTextField - Modify episode by this
     * value args[3] = String - name of database args[4] = String - key you want to
     * modify by args[5] = String[] - keys of values you want to modify
     *
     * @param inputManager
     * @param args
     */
    public ModifyMediaListener(InputManager inputManager, Object[] args) {
        super(inputManager, args);
        this.state = (ViewState) args[0];
        this.nameTextField = (JTextField) args[4];
        this.episodeTextField = (JTextField) args[5];
        modifyCommand = (ModifyCommand) InputManager.COMMAND_FACTORY
                .getCommand(CommandFactory.CommandAction.MODIFY_MEDIA, Arrays.copyOfRange(args, 1, 4));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        doLogic();

        InputManager.COMMAND_FACTORY.executeCommands(new ICommand[] { modifyCommand });

        this.state.updateAspects();
    }

    private void doLogic() {
        System.out.println("Modifying Media Now");
        String showName = this.nameTextField.getText(), episode = this.episodeTextField.getText();

        modifyCommand.setValue(new String[] { episode, showName });
    }
}
