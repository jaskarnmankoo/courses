package _temporary;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

import _backend.commands.ICommand;
import _backend.framework.controllers.InputManager;
import _backend.framework.view.ViewState;
import _backend.utils.database.DBMS;
import _backend.utils.factories.CommandFactory.CommandAction;
import _backend.utils.factories.StateFactory;
import _frontend.userViews.ChatView;

@SuppressWarnings("serial")
public class TestView extends ViewState {
	private Color c;

	public TestView() {
		super("Test");
		this.setLayout(new FlowLayout());

		this.c = new Color((int) (Math.random() * 0x1000000));
		this.setBackground(c);

		JButton button = new JButton("Change Color");
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// button.addActionListener(InputManager.INPUT_LISTENER_FACTORY.getInputListener(InputAction.SWAP_STATE,
		// new Object[] {StateFactory.State.MAIN}));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ICommand swap = InputManager.COMMAND_FACTORY.getCommand(CommandAction.SWAP_STATE,
						new Object[] { StateFactory.State.LOGIN });
				swap.execute();
			}
		});

		JButton button2 = new JButton("Something Else");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread(new ChatView("Test"));
				thread.start();
			}
		});

		this.add(button);
		this.add(button2);
	}

	@Override
	public void updateAspects() {
		/*
		 * Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase.db");
		 * String args[] = {"Faiz"}; // Show case of arguments for preparedStatement
		 * //List<Map<String, Object>> results = DBMS.queryDatabase(conn,
		 * "SELECT * FROM UserAccount WHERE username = ?", args);
		 * //System.out.printf("Username: %s, Password: %s\n",
		 * results.get(0).get("username"), results.get(0).get("password"));
		 * 
		 * List<Map<String, Object>> results = DBMS.queryLocalDatabase(conn,
		 * "SELECT * FROM GlobalMedia WHERE accountname = ?", args); for (Map<String,
		 * Object> r : results) { System.out.printf("User: %s, Show: %s\n",
		 * r.get("accountname"), r.get("medianame")); }
		 * 
		 * DBMS.closeLocalConnection(conn);
		 */

		Connection conn = DBMS.establishServerDatabaseConnection("csc301");
		/*
		 * DBMS.updateDatabase(conn, "CREATE TABLE IF NOT EXISTS UserAccount (" +
		 * "username VARCHAR(50) PRIMARY KEY, " + "password VARCHAR(50) NOT NULL, " +
		 * "email VARCHAR(40));", new String[] {}); DBMS.updateDatabase(conn,
		 * "INSERT INTO UserAccount(username, password) VALUES('Faiz', 'Password');",
		 * new String[] {}); DBMS.updateDatabase(conn,
		 * "INSERT INTO UserAccount(username, password) VALUES('James', 'PokemonMaster');"
		 * , new String[] {});
		 */

		List<Map<String, Object>> results1 = DBMS.queryDatabase(conn, "SELECT * FROM UserAccount", new String[] {});
		List<Map<String, Object>> results2 = DBMS.queryDatabase(conn,
				"SELECT username FROM UserAccount WHERE username = ?", new String[] { "James" });

		for (Map<String, Object> r : results1) {
			System.out.printf("User: %s, Show: %s\n", r.get("username"), r.get("password"));
		}

		for (Map<String, Object> r2 : results2) {
			System.out.printf("User: %s\n", r2.get("username"));
		}

		DBMS.closeConnection(conn);

		this.c = new Color((int) (Math.random() * 0x1000000));
		this.setBackground(c);
	}
}
