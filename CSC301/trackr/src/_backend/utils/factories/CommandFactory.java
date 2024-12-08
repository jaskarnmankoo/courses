package _backend.utils.factories;

import _backend.commands.*;
import _backend.framework.controllers.InputManager;
import _backend.utils.errorHandling.ErrorLogger;

public class CommandFactory {

	public static enum CommandAction {
		SWAP_STATE, ADD_MEDIA, ADD_USER, REMOVE_MEDIA, MODIFY_MEDIA, VALIDATE
	};

	private InputManager inputManager;

	public CommandFactory(InputManager im) {
		this.inputManager = im;
	}

	// In actionPerformed call upon a private method called doSpecificActionStuff,
	// and then InputManager.executeCommands
	public ICommand getCommand(CommandAction ca, Object[] args) {
		ICommand command = null;

		switch (ca) {
		case SWAP_STATE:
			command = new SwapCommand(args);
			break;
		case ADD_MEDIA:
			command = new AddCommand(args);
			break;
		case ADD_USER:
			command = new AddCommand(args);
			break;
		case REMOVE_MEDIA:
			command = new RemoveCommand(args);
			break;
		case MODIFY_MEDIA:
			command = new ModifyCommand(args);
			break;
		default:
			break;
		}

		if (command != null) {
			command.setInputManager(this.inputManager);
		}

		return command;

	}

	public void executeCommands(ICommand[] commands) {
		if (this.inputManager == null)
			ErrorLogger.logMessage("InputManager not set for CommandFactory");

		this.inputManager.executeCommands(commands);
	}
}
