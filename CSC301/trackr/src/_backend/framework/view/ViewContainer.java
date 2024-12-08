package _backend.framework.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import _backend.utils.factories.StateFactory;
import _backend.utils.factories.StateFactory.State;

/**
 * View of MVC
 *
 * JPanel that holds all the ViewStates together in a CardLayout. It also
 * observes any changes that happen in regards to the current ViewState and
 * changes the View accordingly. There is only one ViewContainer class for a run
 * of the application.
 *
 */
@SuppressWarnings("serial")
public class ViewContainer extends JPanel implements Observer {

    public static final int BASE_FRAME_WIDTH = 1280, BASE_FRAME_HEIGHT = 720;

    private static ViewContainer instance = null;

    /**
     * Gets the single instance of ViewContainer. If it does not exist, then create
     * one. Else, return the only one in existence.
     *
     * @return ViewContainer instance
     */
    public static ViewContainer getInstance() {
        if (instance == null) {
            instance = new ViewContainer();
        }

        return instance;
    }

    /**
     * Private Constructor of a ViewContainer. Initializes the Dimensions, Layout,
     * and ViewStates.
     */
    private ViewContainer() {
        super();
        this.setPreferredSize(new Dimension(ViewContainer.BASE_FRAME_WIDTH, ViewContainer.BASE_FRAME_HEIGHT));
        this.setLayout(new CardLayout());
        this.loadStates();
    }

    /*
     * Loads all the states so that we can swap between them.
     */
    private void loadStates() {
        for (State state : StateFactory.State.values()) {
            ViewState viewState = StateFactory.getState(state);
            this.add(viewState, viewState.toString());
        }
    }

    /**
     * Changes the View to another ViewState. Switches based on name.
     * 
     * @param which -> String title of a ViewState
     */
    private void adjustView(String which) {
        CardLayout screens = (CardLayout) this.getLayout();
        screens.show(this, which);
    }

    @Override
    public void update(Observable dnm, Object stateName) {
        this.adjustView((String) stateName); // Safe Cast
    }
}
