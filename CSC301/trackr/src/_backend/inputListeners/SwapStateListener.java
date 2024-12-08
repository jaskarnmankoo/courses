package _backend.inputListeners;

import java.awt.event.ActionEvent;

import _backend.commands.ICommand;
import _backend.framework.controllers.InputManager;
import _backend.utils.factories.CommandFactory.CommandAction;
import _backend.utils.factories.StateFactory.State;

/**
 * This is a specific case of an AbstractListener which specializes in swapping
 * the state from the current one to the one given.
 */
public class SwapStateListener extends AbstractListener {
	private State nextState;
	private ICommand swap;

	public SwapStateListener(InputManager inputManager, Object[] args) {
		super(inputManager, args);

		// Safe cast since a SwapStateListener understands that it will be given a state
		// to swap to
		// this.nextState = (State) this.args[0];

		// this.commands.add(new SwapCommand(inputManager, this.nextState));
		swap = InputManager.COMMAND_FACTORY.getCommand(CommandAction.SWAP_STATE, args);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		InputManager.COMMAND_FACTORY.executeCommands(new ICommand[] { swap });

		/*
		 * System.out.println("Swapping States Now");
		 * //this.inputManager.executeCommands(commands);
		 * this.inputManager.executeCommands(new ICommand[] {this.swap});
		 */
	}
}
