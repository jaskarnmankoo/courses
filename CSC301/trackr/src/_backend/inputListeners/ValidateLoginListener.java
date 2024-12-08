package _backend.inputListeners;

import java.awt.event.ActionEvent;
import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import _backend.framework.controllers.InputManager;
import _backend.framework.controllers.UserManager;
import _backend.utils.database.DBMS;
import _backend.utils.database.UserFileManager;
import _backend.utils.factories.StateFactory;

public class ValidateLoginListener extends AbstractListener {

	private JTextField userNameField;
	private JPasswordField passwordField;

	public ValidateLoginListener(InputManager inputManager, Object[] args) {
		super(inputManager, args);
		this.userNameField = (JTextField) this.args[0];
		this.passwordField = (JPasswordField) this.args[1];
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TEMP
		String[] args = { this.userNameField.getText(), new String(this.passwordField.getPassword()) };

		Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
		boolean in = DBMS.inDatabase(conn, "SELECT * FROM UserAccount WHERE username = ? AND password = ?", args);
		DBMS.closeConnection(conn);

		if (in) {
			// Pull all info down from the postgres database
			UserFileManager.pullUserDatabaseToLocal(this.userNameField.getText());

			// Push any new info up to said database
			UserFileManager.pushUserDatabaseToServer(this.userNameField.getText());

			UserManager.createInstance(this.userNameField.getText());
			inputManager.switchTo(StateFactory.State.MAIN);

			System.out.println("Login Successful");

		} else {
			JOptionPane.showMessageDialog(null, "Username or Password is incorrect.");
			System.out.println("Not a user/ Unable to connect online");
		}
	}
}
