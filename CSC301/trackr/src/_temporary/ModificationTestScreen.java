package _temporary;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import _backend.framework.controllers.InputManager;
import _backend.framework.view.ViewState;
import _backend.utils.database.DBMS;
import _backend.utils.factories.InputListenerFactory.InputAction;
import _backend.utils.factories.StateFactory;

@SuppressWarnings("serial")
public class ModificationTestScreen extends ViewState {
	private JTextArea displayArea;
	public boolean sort = false; // Just for the demo

	public ModificationTestScreen() {
		super("ModificationScreen");
		this.addFeatures();
	}

	private void addFeatures() {
		this.setLayout(new FlowLayout());

		displayArea = new JTextArea(25, 50);
		displayArea.setEditable(false);

		JTextField nameField = new JTextField(10);
		JTextField episodeField = new JTextField(10);
		this.add(nameField);
		this.add(episodeField);

		// Add a Remove, Modify, JButton
		JButton[] buttons = new JButton[5];

		buttons[0] = new JButton("Add");
		buttons[0].addActionListener(InputManager.INPUT_LISTENER_FACTORY.getInputListener(InputAction.ADD_MEDIA,
				new Object[] { this, nameField, episodeField, "PublicList", "(showname, episode)" }));

		buttons[1] = new JButton("Modify");
		buttons[1].addActionListener(InputManager.INPUT_LISTENER_FACTORY.getInputListener(InputAction.MODIFY_MEDIA,
				new Object[] { this, "PublicList", "showname", new String[] { "episode" }, nameField, episodeField }));

		buttons[2] = new JButton("Remove");
		buttons[2].addActionListener(InputManager.INPUT_LISTENER_FACTORY.getInputListener(InputAction.REMOVE_MEDIA,
				new Object[] { this, "PublicList", new String[] { "showname", "episode" }, nameField, episodeField }));

		buttons[3] = new JButton("Sort Alpha");
		buttons[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				sort = true;
				updateAspects();
			}
		});

		buttons[4] = new JButton("Go Back");
		buttons[4].addActionListener(InputManager.INPUT_LISTENER_FACTORY.getInputListener(InputAction.SWAP_STATE,
				new Object[] { StateFactory.State.PROFILE }));

		for (JButton button : buttons) {
			this.add(button);
		}

		this.add(this.displayArea);

	}

	@Override
	public void updateAspects() {
		Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase.db");
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

		if (sort) {
			results = DBMS.queryDatabase(conn, "SELECT * FROM PublicList ORDER BY showname", new String[] {});
		} else {
			results = DBMS.queryDatabase(conn, "SELECT * FROM PublicList", new String[] {});
		}

		String info = "";

		for (Map<String, Object> row : results) {
			info += row.get("showname") + ", " + row.get("episode") + "\n";
		}

		this.displayArea.setText(info);

		sort = false;

		DBMS.closeConnection(conn);

	}
}
