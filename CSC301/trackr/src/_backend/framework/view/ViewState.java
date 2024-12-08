package _backend.framework.view;

import javax.swing.JPanel;

/**
 * Class to represent the differing Views of our Application. Ex. Login, Main,
 * Profile, etc.
 *
 */
@SuppressWarnings("serial")
public abstract class ViewState extends JPanel {
    protected String stateName;

    public ViewState(String viewName) {
        this.stateName = viewName;
    }

    @Override
    public String toString() {
        return this.stateName;
    }

    public abstract void updateAspects();
}
