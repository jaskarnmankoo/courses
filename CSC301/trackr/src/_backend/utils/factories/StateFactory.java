package _backend.utils.factories;

import java.util.EnumMap;

import _backend.framework.view.ViewState;
import _frontend.userViews.LoginView;
import _frontend.userViews.MainView;
import _frontend.userViews.ProfileView;
import _frontend.userViews.RegistrationView;
import _temporary.TestView;

public class StateFactory {
	public static enum State {
		LOGIN, PROFILE, TEST, REGISTRATION, MAIN
	}

	// To Decouple
	private static EnumMap<State, ViewState> statesToView = new EnumMap<State, ViewState>(State.class);
	static {
		statesToView.put(State.LOGIN, new LoginView());
		statesToView.put(State.PROFILE, new ProfileView());
		statesToView.put(State.REGISTRATION, new RegistrationView());
		statesToView.put(State.TEST, new TestView());
		statesToView.put(State.MAIN, new MainView());

	}

	public static ViewState getState(State state) {
		return StateFactory.statesToView.get(state);
	}
}
