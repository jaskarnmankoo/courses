package _frontend.userViews;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;

import _backend.framework.controllers.InputManager;
import _backend.framework.controllers.UserManager;
import _backend.framework.view.ViewState;
import _backend.utils.database.DBMS;
import _backend.utils.factories.InputListenerFactory.InputAction;
import _backend.utils.factories.StateFactory;

public class ProfileView extends ViewState {
	private JTable table;
	private JTextField txtSearchForAnother;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField txtNewFirstName;
	private JTextField txtLastName;
	private JTextField txtEmail;
	private JTextField txtNewPassword;
	private JPasswordField txtCurrentPassword;

	private String chatWith = "";
	private String tableList = "PublicList";

	private JLabel lblFirstName, lblLastName, lblEmail;

	private JLabel lblUserName;

	private JLabel lblTotalTvWatched, lblTimeSpentWatching_1, lblTimeSpentWatching, lblAverageRatingGiven,
			lblTotalMoviesWatched;

	private JButton btnChat;

	private UserManager user;

	private boolean searching = false;

	private DefaultListModel<String> populate_list_two = new DefaultListModel<String>(),
			populate_list = new DefaultListModel<String>();

	/**
	 * Create the panel.
	 */
	public ProfileView() {
		super("Trackr - Profile");

		setBackground(Color.BLACK);
		this.setSize(1280, 721);
		JLabel lblUserAvatar = new JLabel("");
		lblUserAvatar.setBounds(12, 103, 217, 241);
		lblUserAvatar.setIcon(new ImageIcon("resources/images/ilir_logo.png"));
		lblUserAvatar.setForeground(Color.WHITE);
		lblUserAvatar.setBackground(Color.WHITE);
		lblUserAvatar.setBorder(new LineBorder(Color.WHITE));

		lblUserName = new JLabel(String.format("/%s's PROFILE/", "ILIR"));
		lblUserName.setBounds(6, 26, 425, 22);
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 18));

		txtSearchForAnother = new JTextField();
		txtSearchForAnother.setToolTipText("You can search for another user here");
		txtSearchForAnother.setBounds(98, 68, 212, 31);
		txtSearchForAnother.setColumns(10);

		JButton btnHome = new JButton("Home");

		btnHome.setBounds(12, 69, 84, 28);
		btnHome.addActionListener(InputManager.INPUT_LISTENER_FACTORY.getInputListener(InputAction.SWAP_STATE,
				new Object[] { StateFactory.State.MAIN }));

		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(309, 69, 89, 28);
		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				if (!txtSearchForAnother.getText().equals("")) {
					searching = true;
				} else {
					searching = false;
				}
				updateAspects();
			}

		});

		JPanel pnlInfoContainer = new JPanel();
		pnlInfoContainer.setBounds(6, 371, 392, 309);
		pnlInfoContainer.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		pnlInfoContainer.setBackground(Color.LIGHT_GRAY);

		JLabel lblFirstNameAvatarDisplay = new JLabel("First Name: ");
		lblFirstNameAvatarDisplay.setBounds(241, 109, 123, 21);
		lblFirstNameAvatarDisplay.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblFirstNameAvatarDisplay.setForeground(Color.WHITE);

		JLabel lblLastNameAvatarDisplay = new JLabel("Last Name:");
		lblLastNameAvatarDisplay.setBounds(241, 193, 123, 21);
		lblLastNameAvatarDisplay.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblLastNameAvatarDisplay.setForeground(Color.WHITE);

		JLabel lblEmailAvatarDisplay = new JLabel("Email:");
		lblEmailAvatarDisplay.setBounds(241, 269, 84, 13);
		lblEmailAvatarDisplay.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblEmailAvatarDisplay.setForeground(Color.WHITE);

		lblTotalTvWatched = new JLabel("");
		lblTotalTvWatched.setForeground(Color.BLACK);
		lblTotalTvWatched.setFont(new Font("SansSerif", Font.PLAIN, 16));

		lblTimeSpentWatching_1 = new JLabel("Time spent watching Movies: ");
		lblTimeSpentWatching_1.setForeground(Color.BLACK);
		lblTimeSpentWatching_1.setFont(new Font("SansSerif", Font.PLAIN, 16));

		lblTimeSpentWatching = new JLabel("Time spent watching TV: ");
		lblTimeSpentWatching.setForeground(Color.BLACK);
		lblTimeSpentWatching.setFont(new Font("SansSerif", Font.PLAIN, 16));

		lblAverageRatingGiven = new JLabel("Average rating given: ");
		lblAverageRatingGiven.setForeground(Color.BLACK);
		lblAverageRatingGiven.setFont(new Font("SansSerif", Font.PLAIN, 16));

		lblTotalMoviesWatched = new JLabel("Total Movies Watched: ");
		lblTotalMoviesWatched.setForeground(Color.BLACK);
		lblTotalMoviesWatched.setFont(new Font("SansSerif", Font.PLAIN, 16));

		JLabel lblFavouriteGenre = new JLabel("Favourite Genre: Horror");
		lblFavouriteGenre.setForeground(Color.BLACK);
		lblFavouriteGenre.setFont(new Font("SansSerif", Font.PLAIN, 16));

		GroupLayout gl_pnlInfoContainer = new GroupLayout(pnlInfoContainer);
		gl_pnlInfoContainer.setHorizontalGroup(gl_pnlInfoContainer.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInfoContainer.createSequentialGroup().addContainerGap().addGroup(gl_pnlInfoContainer
						.createParallelGroup(Alignment.LEADING).addComponent(lblTotalTvWatched)
						.addComponent(lblTimeSpentWatching_1, GroupLayout.PREFERRED_SIZE, 295,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTimeSpentWatching, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAverageRatingGiven, GroupLayout.PREFERRED_SIZE, 260,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTotalMoviesWatched, GroupLayout.PREFERRED_SIZE, 271,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFavouriteGenre, GroupLayout.PREFERRED_SIZE, 252, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(81, Short.MAX_VALUE)));
		gl_pnlInfoContainer.setVerticalGroup(gl_pnlInfoContainer.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInfoContainer.createSequentialGroup().addContainerGap().addComponent(lblTotalTvWatched)
						.addGap(18).addComponent(lblTimeSpentWatching_1).addGap(25).addComponent(lblTimeSpentWatching)
						.addGap(18).addComponent(lblAverageRatingGiven).addGap(28).addComponent(lblTotalMoviesWatched)
						.addGap(18).addComponent(lblFavouriteGenre).addContainerGap(81, Short.MAX_VALUE)));

		pnlInfoContainer.setLayout(gl_pnlInfoContainer);

		Connection friend_conn = DBMS.establishServerDatabaseConnection("csc301");
		if (friend_conn == null) {
			populate_list.addElement("You are not connected, so you have no friends to talk with");
		} else {
			List<Map<String, Object>> friend_results = DBMS.queryDatabase(friend_conn, "SELECT * FROM UserAccount",
					new String[] {});

			for (Map<String, Object> friend : friend_results) {
				populate_list.addElement((String) friend.get("username"));
			}

			DBMS.closeConnection(friend_conn);
		}

		JList jList = new JList(populate_list);

		jList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chatWith = (String) jList.getSelectedValue();
			}
		});

		JScrollPane lstFriendsContainer = new JScrollPane(jList);
		lstFriendsContainer.setBounds(454, 94, 807, 188);
		lstFriendsContainer.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		lstFriendsContainer.setBackground(Color.LIGHT_GRAY);

		JPanel pnlUpdateContainer = new JPanel();
		pnlUpdateContainer.setBounds(454, 295, 270, 385);
		pnlUpdateContainer.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		pnlUpdateContainer.setBackground(Color.LIGHT_GRAY);

		// here goes the connection code

		JList jList_two = new JList(populate_list_two);

		jList_two.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Thread thread = new Thread(new ModView((String) jList_two.getSelectedValue(), 0)); // 0 being from
																										// publiclist
					thread.start();
				}
			}
		});

		JScrollPane lstPublicPrivateContainer = new JScrollPane(jList_two);
		lstPublicPrivateContainer.setBounds(730, 295, 531, 385);
		lstPublicPrivateContainer.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		lstPublicPrivateContainer.setBackground(Color.LIGHT_GRAY);

		btnChat = new JButton("Chat");
		btnChat.setBounds(538, 63, 70, 28);
		btnChat.setToolTipText("Click on a friend in the List then this to start a private chat session with them!");
		btnChat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chatWith == "") {
					System.out.println("No one selected to talk with : (");
				} else {
					Thread thread = new Thread(new ChatView(chatWith));
					thread.start();
				}

			}

		});

		JLabel lblFriendsList = new JLabel("Friend's list");
		lblFriendsList.setBounds(454, 66, 79, 20);
		lblFriendsList.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFriendsList.setForeground(Color.WHITE);

		JRadioButton rdbtnPublicList = new JRadioButton("Public List");
		buttonGroup.add(rdbtnPublicList);
		rdbtnPublicList.setSelected(true);
		rdbtnPublicList.setBounds(742, 686, 155, 26);
		rdbtnPublicList.setFont(new Font("SansSerif", Font.PLAIN, 20));
		rdbtnPublicList.setForeground(Color.WHITE);
		rdbtnPublicList.addActionListener(new ActionListener() { // Changes tablename to select from public

			@Override
			public void actionPerformed(ActionEvent e) {
				tableList = "PublicList";
				updateAspects();

			}

		});

		JRadioButton rdbtnPrivateList = new JRadioButton("Private List");
		buttonGroup.add(rdbtnPrivateList);
		rdbtnPrivateList.setBounds(890, 686, 173, 26);
		rdbtnPrivateList.setFont(new Font("SansSerif", Font.PLAIN, 20));
		rdbtnPrivateList.setForeground(Color.WHITE);
		rdbtnPrivateList.addActionListener(new ActionListener() { // Changes tablename to select from private list.

			@Override
			public void actionPerformed(ActionEvent e) {
				tableList = "PrivateList";
				updateAspects();

			}

		});

		JLabel lblAttributesWith = new JLabel("Attributes with * are mandatory");
		lblAttributesWith.setBounds(460, 686, 193, 16);
		lblAttributesWith.setForeground(Color.WHITE);

		lblFirstName = new JLabel("First name ");
		lblFirstName.setBounds(241, 148, 123, 26);
		lblFirstName.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblFirstName.setForeground(Color.WHITE);

		lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(241, 225, 123, 26);
		lblLastName.setForeground(Color.WHITE);
		lblLastName.setFont(new Font("SansSerif", Font.PLAIN, 16));

		lblEmail = new JLabel("Email");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblEmail.setBounds(241, 302, 212, 26);

		JLabel lblUpdateInformation = new JLabel("Update Information");
		lblUpdateInformation.setFont(new Font("Modern No. 20", Font.PLAIN, 14));

		JLabel lblFirstNameUpdateInfo = new JLabel("First Name:");

		txtNewFirstName = new JTextField();
		txtNewFirstName.setColumns(10);

		JLabel lblLastNameUpdateInfo = new JLabel("Last Name:");

		txtLastName = new JTextField();
		txtLastName.setColumns(10);

		JLabel lblEmailUpdateInfo = new JLabel("Email:");

		txtEmail = new JTextField();
		txtEmail.setColumns(10);

		JLabel lblNewPassword = new JLabel("New Password:");

		txtNewPassword = new JTextField();
		txtNewPassword.setColumns(10);

		JLabel lblCurrentPassword = new JLabel("Current Password: *");

		txtCurrentPassword = new JPasswordField();
		txtCurrentPassword.setColumns(10);

		JButton btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Starting to change User Information...");
				Connection conn = DBMS.establishServerDatabaseConnection("csc301");

				if (conn == null) {
					System.out.println("You, are not connected because of shitty UOFT internet");
					return;
				}

				if (!(DBMS.inDatabase(conn, "SELECT * FROM UserAccount WHERE username=? AND password=?",
						new String[] { user.username(), new String(txtCurrentPassword.getPassword()) }))) {
					System.out.println("That is not the correct current password. No updating will be done.");
					DBMS.closeConnection(conn);
					return;
				}

				DBMS.updateDatabase(conn,
						"UPDATE UserAccount set password=?, firstname=?, lastname=?, email=? WHERE username=?",
						new String[] { txtNewPassword.getText(), txtNewFirstName.getText(), txtLastName.getText(),
								txtEmail.getText(), user.username() });

				Connection conn_two = DBMS.establishLocalDatabaseConnection("TrackrDatabase");

				DBMS.updateDatabase(conn_two,
						"UPDATE UserAccount set password=?, firstname=?, lastname=?, email=? WHERE username=?",
						new String[] { txtNewPassword.getText(), txtNewFirstName.getText(), txtLastName.getText(),
								txtEmail.getText(), user.username() });

				DBMS.closeConnection(conn_two);
				DBMS.closeConnection(conn);

				UserManager.disposeInstance();
				user = UserManager.createInstance(user.username());
				updateAspects();

			}

		});
		GroupLayout gl_pnlUpdateContainer = new GroupLayout(pnlUpdateContainer);
		gl_pnlUpdateContainer.setHorizontalGroup(gl_pnlUpdateContainer.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlUpdateContainer.createSequentialGroup().addGroup(gl_pnlUpdateContainer
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlUpdateContainer.createSequentialGroup().addGap(94)
								.addComponent(lblFirstNameUpdateInfo))
						.addGroup(gl_pnlUpdateContainer.createSequentialGroup().addGap(73)
								.addComponent(lblUpdateInformation))
						.addGroup(gl_pnlUpdateContainer.createSequentialGroup().addGap(94).addComponent(
								lblLastNameUpdateInfo, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_pnlUpdateContainer.createSequentialGroup().addGap(42)
								.addGroup(gl_pnlUpdateContainer.createParallelGroup(Alignment.LEADING)
										.addComponent(txtLastName, GroupLayout.PREFERRED_SIZE, 161,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(txtNewFirstName, GroupLayout.PREFERRED_SIZE, 161,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 161,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(txtNewPassword, GroupLayout.PREFERRED_SIZE, 161,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(txtCurrentPassword, GroupLayout.PREFERRED_SIZE, 161,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_pnlUpdateContainer.createSequentialGroup().addGap(104)
								.addComponent(lblEmailUpdateInfo))
						.addGroup(gl_pnlUpdateContainer.createSequentialGroup().addGap(80).addComponent(lblNewPassword))
						.addGroup(gl_pnlUpdateContainer.createSequentialGroup().addGap(72)
								.addComponent(lblCurrentPassword))
						.addGroup(
								gl_pnlUpdateContainer.createSequentialGroup().addGap(67).addComponent(btnSaveChanges)))
						.addContainerGap(57, Short.MAX_VALUE)));
		gl_pnlUpdateContainer.setVerticalGroup(gl_pnlUpdateContainer.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlUpdateContainer.createSequentialGroup().addContainerGap()
						.addComponent(lblUpdateInformation).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblFirstNameUpdateInfo).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtNewFirstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblLastNameUpdateInfo)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtLastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblEmailUpdateInfo)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblNewPassword)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtNewPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblCurrentPassword)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(txtCurrentPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnSaveChanges)
						.addContainerGap(9, Short.MAX_VALUE)));
		pnlUpdateContainer.setLayout(gl_pnlUpdateContainer);
		setLayout(null);
		add(btnHome);
		add(txtSearchForAnother);
		add(btnSearch);
		add(lblUserAvatar);
		add(lblLastName);
		add(lblLastNameAvatarDisplay);
		add(lblFirstNameAvatarDisplay);
		add(lblEmailAvatarDisplay);
		add(lblFirstName);
		add(pnlInfoContainer);
		add(lblFriendsList);
		add(btnChat);
		add(lstFriendsContainer);
		add(pnlUpdateContainer);
		add(lblAttributesWith);
		add(rdbtnPublicList);
		add(rdbtnPrivateList);
		add(lstPublicPrivateContainer);
		add(lblUserName);
		add(lblEmail);

		table = new JTable();
	}

	@Override
	public void updateAspects() {
		if (user == null)
			user = UserManager.getInstance();

		Path folderPath = Paths
				.get(String.format("%s/IlistDemedia/%s.db", System.getProperty("user.home"), user.username()));
		File file = folderPath.toFile();
		if (!file.exists()) { // Checks for the existence of the folder
			return;
		}
		// Profile Information
		populate_list.removeElement(user.username());

		lblUserName.setText(String.format("/%s's PROFILE/", user.username().toUpperCase()));
		lblFirstName.setText(user.firstname());
		lblLastName.setText(user.lastname());
		lblEmail.setText(user.email());

		// Friends
		populate_list.clear();
		Connection friend_conn = DBMS.establishServerDatabaseConnection("csc301");
		if (friend_conn == null) {
			populate_list.addElement("You are not connected, so you have no friends to talk with");
			this.btnChat.setEnabled(false);

		} else {
			List<Map<String, Object>> friend_results;

			if (!searching) {
				friend_results = DBMS.queryDatabase(friend_conn, "SELECT * FROM UserAccount", new String[] {});
			} else {
				friend_results = DBMS.queryDatabase(friend_conn, "SELECT * FROM UserAccount WHERE username=?",
						new String[] { txtSearchForAnother.getText() });
			}

			for (Map<String, Object> friend : friend_results) {
				populate_list.addElement((String) friend.get("username"));
			}

			DBMS.closeConnection(friend_conn);
		}

		// Public Private List
		populate_list_two.clear();
		Connection conn = DBMS.establishLocalDatabaseConnection(UserManager.getInstance().username()); // this should be
																										// UserManager.getInstance()
																										// but i'm only
																										// getting ilir
																										// :thinking:
		List<Map<String, Object>> results = DBMS.queryDatabase(conn, "SELECT * FROM " + tableList, new String[] {});
		DBMS.closeConnection(conn);

		if (results != null) {
			for (Map<String, Object> result : results) {
				populate_list_two.addElement((String) result.get("mediatitle"));
			}
		}

		Connection loadingInfo = DBMS.establishLocalDatabaseConnection(UserManager.getInstance().username());
		Connection rating = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
		List<Map<String, Object>> result_tv = DBMS.queryDatabase(loadingInfo,
				"SELECT COUNT(mediatitle) as publicCount FROM PublicList WHERE mediatype='TV Show'", new String[] {});

		String i = (result_tv.get(0).get("publicCount") == null || result_tv.size() == 0) ? "N/A"
				: Integer.toString((int) result_tv.get(0).get("publicCount"));
		lblTotalTvWatched.setText("Total TV Shows Watched: " + i);

		result_tv = DBMS.queryDatabase(loadingInfo,
				"SELECT SUM(mediaduration) as publicCount FROM PublicList WHERE mediatype='Movie'", new String[] {});
		i = (result_tv.get(0).get("publicCount") == null || result_tv.size() == 0) ? "N/A"
				: Integer.toString((int) result_tv.get(0).get("publicCount"));
		lblTimeSpentWatching_1.setText("Time spent watching Movies: " + i);

		result_tv = DBMS.queryDatabase(loadingInfo,
				"SELECT SUM(mediaduration) as total FROM PublicList WHERE mediatype='TV Show'", new String[] {});
		i = (result_tv.get(0).get("total") == null || result_tv.size() == 0) ? "N/A"
				: Integer.toString((int) result_tv.get(0).get("total"));
		lblTimeSpentWatching.setText("Time spent watching TV: " + i);

		result_tv = DBMS.queryDatabase(rating, "SELECT AVG(rating) as avg FROM Ratings WHERE username=?",
				new String[] { UserManager.getInstance().username() });
		i = (result_tv.get(0).get("avg") == null || result_tv.size() == 0) ? "N/A"
				: Double.toString((double) result_tv.get(0).get("avg"));
		lblAverageRatingGiven.setText("Average rating given: " + i);

		result_tv = DBMS.queryDatabase(loadingInfo,
				"SELECT COUNT(mediatitle) as publicSum FROM PublicList WHERE mediatype='Movie'", new String[] {});
		i = (result_tv.get(0).get("publicSum") == null || result_tv.size() == 0) ? "N/A"
				: Integer.toString((int) result_tv.get(0).get("publicSum"));
		lblTotalMoviesWatched.setText("Total Movies Watched: " + i);

		DBMS.closeConnection(loadingInfo);
		DBMS.closeConnection(rating);
	}
}
