package _frontend.userViews;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import _backend.commands.ICommand;
import _backend.framework.controllers.InputManager;
import _backend.framework.controllers.UserManager;
import _backend.framework.view.ViewState;
import _backend.utils.database.UserFileManager;
import _backend.utils.factories.CommandFactory.CommandAction;
import _backend.utils.factories.InputListenerFactory.InputAction;
import _backend.utils.factories.StateFactory;

public class LoginView extends ViewState {
	private JTextField username;
	private JTextField password;

	/**
	 * Create the application.
	 */
	public LoginView() {
		super("Login to Trackr");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		this.setBackground(Color.black);
		this.setSize(1280, 720);
		JLabel lblUsernameOrEmail = new JLabel("Username or Email");
		lblUsernameOrEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUsernameOrEmail.setBounds(38, 38, 152, 24);
		lblUsernameOrEmail.setForeground(Color.white);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPassword.setBounds(38, 110, 113, 32);
		lblPassword.setForeground(Color.white);

		username = new JTextField();
		username.setFont(new Font("SansSerif", Font.PLAIN, 16));
		username.setBounds(38, 58, 188, 32);
		username.setColumns(10);

		password = new JPasswordField(10);
		password.setFont(new Font("SansSerif", Font.PLAIN, 16));
		password.setBounds(38, 136, 188, 32);

		JLabel lblNewUser = new JLabel("New User? Sign Up!");
		lblNewUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ICommand swap = InputManager.COMMAND_FACTORY.getCommand(CommandAction.SWAP_STATE,
						new Object[] { StateFactory.State.REGISTRATION });
				swap.execute();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblNewUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

		});
		lblNewUser.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewUser.setForeground(Color.white);

		JLabel lblBrowseAsGuest = new JLabel("Browse as Guest");
		lblBrowseAsGuest.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				try {
					UserManager.createInstance(UserManager.ILIR);
					System.out.println("I am here");
					UserFileManager.initializeDatabase(System.getProperty("user.home"), UserManager.ILIR,
							UserFileManager.USER_DATABASE);
					ICommand swap = InputManager.COMMAND_FACTORY.getCommand(CommandAction.SWAP_STATE,
							new Object[] { StateFactory.State.MAIN });

					swap.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblBrowseAsGuest.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		lblBrowseAsGuest.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBrowseAsGuest.setForeground(Color.white);

		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnLogin.addActionListener(InputManager.INPUT_LISTENER_FACTORY.getInputListener(InputAction.VALIDATE,
				new Object[] { username, password }));
		btnLogin.setBounds(157, 192, 125, 38);
		btnLogin.setActionCommand("Login");

		JLabel lblTrackr = new JLabel("Ilist Demedia");
		lblTrackr.setFont(new Font("Tahoma", Font.PLAIN, 56));
		lblTrackr.setForeground(Color.white);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(433)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUsernameOrEmail, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTrackr, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(username, Alignment.LEADING).addComponent(password,
												Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
								.addGap(450))
						.addComponent(lblNewUser, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
						.addComponent(lblBrowseAsGuest, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 841,
								Short.MAX_VALUE))
				.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap(75, Short.MAX_VALUE)
						.addComponent(lblTrackr, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE).addGap(82)
						.addComponent(lblUsernameOrEmail).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(username, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).addGap(42)
						.addComponent(lblPassword).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(password, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE).addGap(32)
						.addComponent(lblNewUser).addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(lblBrowseAsGuest).addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(btnLogin).addGap(212)));
		this.setLayout(groupLayout);
	}

	@Override
	public void updateAspects() {
	}

	/**
	 * Getter for Username TextField
	 * 
	 * @return username field
	 */
	public JTextField getUserField() {
		return this.username;
	}

	/**
	 * Getter for password TextField
	 * 
	 * @return password field
	 */
	public JTextField getPassField() {
		return this.password;
	}
}
