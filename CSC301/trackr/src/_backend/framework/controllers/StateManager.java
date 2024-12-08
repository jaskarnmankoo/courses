package _backend.framework.controllers;

import java.util.Observable;

import _backend.framework.view.ViewContainer;
import _backend.framework.view.ViewState;
import _backend.utils.errorHandling.ErrorLogger;
import _backend.utils.factories.StateFactory;
import _backend.utils.factories.StateFactory.State;

/**
 * Manages all aspects of States and what is the current state and transitions.
 *
 */
public class StateManager extends Observable {
    private ViewState currentState; // State the User is currently seeing
    private static StateManager instance = null; // Singleton StateManager

    /**
     * Private constructor to create an instance of StateManager. Sets up the
     * connection between StateManager and ViewContainer.
     *
     * Should start off on LoginScreen.
     *
     * @param container -> ViewContainer to Observe State changes
     */
    private StateManager(ViewContainer container) {
        if (container == null) {
            ErrorLogger.logError(new IllegalArgumentException("Issue constructing StateManager: ViewContainer null."));
        }

        this.addObserver(container);
    }

    /**
     * Returns the only instance of this class.
     *
     * Public Method which is the only way you can access the StateManager.
     * 
     * @return
     */

    static StateManager getInstance() {
        if (instance == null) {
            instance = new StateManager(ViewContainer.getInstance());
        }

        return instance;
    }

    /**
     * Sets up the current state to display *newState*.
     *
     * @param newState -> new State to display
     */
    public void swapState(State newState) {
        this.currentState = StateFactory.getState(newState);
        System.out.println(this.currentState.toString());
        System.out.println(newState);
        this.currentState.updateAspects();
        this.setChanged();
        this.notifyObservers(this.currentState.toString());
    }

    /**
     * Returns the active State.
     *
     * @return ViewState
     */
    public ViewState getCurrentState() {
        return this.currentState;
    }

}
