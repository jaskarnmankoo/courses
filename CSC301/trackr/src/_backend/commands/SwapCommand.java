package _backend.commands;

import _backend.framework.controllers.InputManager;
import _backend.framework.controllers.StateManager;
import _backend.utils.factories.StateFactory;
import _backend.utils.factories.StateFactory.State;

/**
 * Changes the current View to another View.
 *
 */
public class SwapCommand implements ICommand {
    private StateFactory.State state; // State to change to
    private InputManager inputManager; // InputManager instance

    /**
     * Constructor for SwapCommand that specifies what state to switch to when the
     * command is invoked.
     *
     * @param im    -> InputManager instance
     * @param state -> state to switch to
     */
    public SwapCommand(InputManager im, StateFactory.State state) {
        this.state = state;
        this.inputManager = im;
    }

    public SwapCommand(Object[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException();
        }

        if (!(args[0] instanceof StateFactory.State)) {
            // throw new IllegalArgumentException();
        }

        this.state = (StateFactory.State) args[0];
    }

    /**
     * Changes state
     */
    @Override
    public void execute() {
        this.inputManager.switchTo(state);
    }

    @Override
    public void setInputManager(InputManager im) {
        this.inputManager = im;
    }
}
