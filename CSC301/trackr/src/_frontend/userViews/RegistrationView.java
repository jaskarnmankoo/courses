package _frontend.userViews;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

import _backend.commands.ICommand;
import _backend.framework.controllers.InputManager;
import _backend.framework.view.ViewState;
import _backend.utils.database.DBMS;
import _backend.utils.database.UserFileManager;
import _backend.utils.factories.CommandFactory.CommandAction;
import _backend.utils.factories.StateFactory;

@SuppressWarnings("serial")
public class RegistrationView extends ViewState {
	private JTextField txtFirstName;
	private JPasswordField txtPssChoosePassword;
	private JTextField txtLastName;
	private JTextField txtEmail;
	private JTextField textField;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/*
	 * RegistrationScreen()
	 *
	 * This screen is linked to the login page and only the login page A user will
	 * enter their data and a new user will be saved in the database Once
	 * registered, the viewState will return to the login screen A user will need to
	 * enter their Name, Gender, Email, Username, and Password
	 */
	public RegistrationView() {
		super("Trackr - New Account");
		setBackground(Color.black);
		this.setSize(1280, 720);
		JPanel panel = new JPanel();
		panel.setBackground(Color.black);

		JLabel lblWelcomePleaseRegister = new JLabel("New User Registration");
		lblWelcomePleaseRegister.setBackground(Color.BLACK);
		lblWelcomePleaseRegister.setForeground(Color.WHITE);
		lblWelcomePleaseRegister.setFont(new Font("Modern No. 20", Font.BOLD, 56));

		JLabel lblFirstName = new JLabel("First Name: *");
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFirstName.setBackground(Color.BLACK);
		lblFirstName.setForeground(Color.WHITE);

		txtFirstName = new JTextField();
		txtFirstName.setBackground(Color.WHITE);
		txtFirstName.setForeground(Color.BLACK);
		txtFirstName.setBorder(UIManager.getBorder("FormattedTextField.border"));
		txtFirstName.setColumns(10);

		JLabel lblLastName = new JLabel("Last Name: *");
		lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLastName.setBackground(Color.BLACK);
		lblLastName.setForeground(Color.WHITE);

		txtLastName = new JTextField();
		txtLastName.setColumns(10);

		JLabel lblEmail = new JLabel("Email: *");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmail.setBackground(Color.BLACK);
		lblEmail.setForeground(Color.WHITE);

		txtEmail = new JTextField();
		txtEmail.setColumns(10);

		JLabel lblUserName = new JLabel("Username: *");
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUserName.setBackground(Color.BLACK);
		lblUserName.setForeground(Color.WHITE);

		textField = new JTextField();
		textField.setColumns(10);

		JLabel lblChooseAPassword = new JLabel("Choose a password: *");
		lblChooseAPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblChooseAPassword.setBackground(Color.BLACK);
		lblChooseAPassword.setForeground(Color.WHITE);

		txtPssChoosePassword = new JPasswordField();
		txtPssChoosePassword.setColumns(10);

		JButton btnRegisterNow = new JButton("Register now!");
		btnRegisterNow.setBackground(UIManager.getColor("Button.background"));

		btnRegisterNow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					// Add into Server database here
					Connection serverConn = DBMS.establishServerDatabaseConnection("csc301");
					if (serverConn == null) {
						JOptionPane.showMessageDialog(null, "Can't Register At This Time. Are you Offline?");
						System.out.println("Can't Register At This Time");
						return;
					}

					if (DBMS.inDatabase(serverConn, "SELECT * FROM UserAccount WHERE username=?",
							new String[] { textField.getText() })) {
						JOptionPane.showMessageDialog(null, "User Already exists, try again.");
						DBMS.closeConnection(serverConn);
						return;
					}

					DBMS.updateDatabase(serverConn, "INSERT INTO UserAccount VALUES(?, ?, ?, ?, ?)",
							new String[] { textField.getText(), new String(txtPssChoosePassword.getText()),
									txtEmail.getText(), txtFirstName.getText(), txtLastName.getText() });
					DBMS.closeConnection(serverConn);

					// Add into Local here
					Connection localConn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
					if (localConn == null) {
						JOptionPane.showMessageDialog(null,
								"Can't Register At This Time. Local database connection could not be made.");
						System.out.println("Can't Register At This Time");
						DBMS.closeConnection(serverConn);
						return;
					}

					DBMS.updateDatabase(localConn, "INSERT INTO UserAccount VALUES(?, ?, ?, ?, ?)",
							new String[] { textField.getText(), new String(txtPssChoosePassword.getPassword()),
									txtEmail.getText(), txtFirstName.getText(), txtLastName.getText() });
					DBMS.closeConnection(localConn);

					// User has now registered so add his/her username.db to their computer and then
					// make it on the postgres server as well
					UserFileManager.initializeDatabase(System.getProperty("user.home"), textField.getText(),
							UserFileManager.USER_DATABASE);
					Connection conn = DBMS.establishServerDatabaseConnection("");
					String createDB = "CREATE DATABASE " + textField.getText();
					DBMS.updateDatabase(conn, createDB, new String[] {});
					DBMS.closeConnection(conn);

					// load schema and sync with postgres
					UserFileManager.pushUserDatabaseToServer(textField.getText().toLowerCase());

					// Swap back to login after we are done
					ICommand swap = InputManager.COMMAND_FACTORY.getCommand(CommandAction.SWAP_STATE,
							new Object[] { StateFactory.State.LOGIN });
					swap.execute();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addGap(7).addComponent(panel, GroupLayout.DEFAULT_SIZE, 1273, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
						.addGap(7).addComponent(panel, GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)));

		JButton btnNewButton = new JButton("< Back");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ICommand swap = InputManager.COMMAND_FACTORY.getCommand(CommandAction.SWAP_STATE,
						new Object[] { StateFactory.State.LOGIN });
				InputManager.COMMAND_FACTORY.executeCommands(new ICommand[] { swap });
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup()
						.addGap(496)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false).addComponent(lblFirstName)
								.addComponent(txtFirstName, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
								.addComponent(lblLastName)
								.addComponent(txtLastName, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
								.addComponent(lblEmail)
								.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUserName)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblChooseAPassword)
								.addComponent(txtPssChoosePassword, GroupLayout.PREFERRED_SIZE, 275,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel.createSequentialGroup().addComponent(btnNewButton)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnRegisterNow))))
						.addGroup(gl_panel.createSequentialGroup().addGap(331).addComponent(lblWelcomePleaseRegister)))
				.addContainerGap(372, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(34).addComponent(lblWelcomePleaseRegister).addGap(18)
						.addComponent(lblFirstName).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtFirstName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(23).addComponent(lblLastName).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtLastName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(lblEmail).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(18)
						.addComponent(lblUserName).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(18)
						.addComponent(lblChooseAPassword).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtPssChoosePassword, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btnNewButton)
								.addComponent(btnRegisterNow))
						.addGap(197)));
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

	}

	@Override
	public void updateAspects() {
		System.out.println("Registered New User");
	}
}
