package _backend.utils.factories;

import _backend.framework.controllers.InputManager;
import _backend.inputListeners.AbstractListener;
import _backend.inputListeners.AddMediaListener;
import _backend.inputListeners.AddUserListener;
import _backend.inputListeners.ModifyMediaListener;
import _backend.inputListeners.RemoveMediaListener;
import _backend.inputListeners.SwapStateListener;
import _backend.inputListeners.ValidateLoginListener;

public class InputListenerFactory {
	public static enum InputAction {
		SWAP_STATE, ADD_MEDIA, ADD_USER, REMOVE_MEDIA, MODIFY_MEDIA, VALIDATE, ADD_RATING
	};

	private InputManager inputManager; // Glue to help until OurStubbornLeader makes Commands

	public InputListenerFactory(InputManager inputManager) {
		this.inputManager = inputManager;
	}

	public AbstractListener getInputListener(InputAction action, Object[] args) {
		AbstractListener listener = null;

		switch (action) {
		case SWAP_STATE:
			listener = new SwapStateListener(this.inputManager, args);
			break;

		case ADD_MEDIA:
			listener = new AddMediaListener(this.inputManager, args);
			break;

		case ADD_USER:
			listener = new AddUserListener(this.inputManager, args);
			break;

		case REMOVE_MEDIA:
			listener = new RemoveMediaListener(this.inputManager, args);
			break;

		case MODIFY_MEDIA:
			listener = new ModifyMediaListener(this.inputManager, args);
			break;

		case VALIDATE:
			listener = new ValidateLoginListener(this.inputManager, args);
			break;

		default:
			break;

		}

		return listener;
	}
}
