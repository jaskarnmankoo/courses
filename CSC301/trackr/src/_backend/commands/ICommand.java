package _backend.commands;

import _backend.framework.controllers.InputManager;

public interface ICommand {
    public void execute();

    public void setInputManager(InputManager im);
}
