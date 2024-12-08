package _backend.inputListeners;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import _backend.commands.ICommand;
import _backend.framework.controllers.InputManager;

/**
 * This class allows for abstraction of variables between listeners and lets
 * them work abstractly, without conflict of what types they need to work.
 */
public abstract class AbstractListener implements ActionListener {
    // An Abstract Listener takes an arbitrary number of arguments
    // Each child listener understands what arguments it should need
    // to accomplish its task
    protected Object[] args;
    protected InputManager inputManager;
    protected List<ICommand> commands = new ArrayList<ICommand>();

    public AbstractListener(InputManager inputManager, Object[] args) {
        this.args = args;
        this.inputManager = inputManager;
    }
}
