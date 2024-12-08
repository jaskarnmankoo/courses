package _frontend.userViews;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import _backend.framework.controllers.InputManager;
import _backend.framework.view.ViewState;
import _backend.utils.database.DBMS;
import _backend.utils.factories.InputListenerFactory.InputAction;
import _backend.utils.factories.StateFactory;

public class MainView extends ViewState {
	private JTextField txtSearchForMovies;
	private JScrollPane scrollPane, scrollPane_1;
	private final ButtonGroup ViewPaneButtonGroup = new ButtonGroup();
	private DefaultListModel<String> populate_list = new DefaultListModel<String>();
	private JList JLst;

	public MainView() {
		super("Ilist Demedia");
		setBackground(Color.BLACK);
		this.setSize(1280, 720);

		txtSearchForMovies = new JTextField();
		txtSearchForMovies.setText("Search for Movies, TV shows, etc");
		txtSearchForMovies.setColumns(10);

		JButton btnMyList = new JButton("More Info");
		btnMyList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String show = (String) JLst.getSelectedValue();

			}
		});

		JButton btnNewButton = new JButton("My Profile");
		btnNewButton.addActionListener(InputManager.INPUT_LISTENER_FACTORY.getInputListener(InputAction.SWAP_STATE,
				new Object[] { StateFactory.State.PROFILE }));

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		panel.setBackground(Color.LIGHT_GRAY);

		JLabel lblIlirsPicks = new JLabel("Ilir's Picks");
		lblIlirsPicks.setForeground(Color.WHITE);
		lblIlirsPicks.setFont(new Font("Modern No. 20", Font.PLAIN, 36));

		scrollPane = new JScrollPane();

		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		scrollPane.setBackground(Color.LIGHT_GRAY);
		scrollPane.createVerticalScrollBar();

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		scrollPane_1.setBackground(Color.LIGHT_GRAY);

		JLabel lblMovie2 = new JLabel();
		lblMovie2.setIcon(new ImageIcon("resources/images/wargames_poster.jpg"));
		lblMovie2.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblMovie2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new MediaScreen("WarGames"));
				thread.start();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblMovie2.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

		});

		JLabel lblMovie3 = new JLabel();
		lblMovie3.setIcon(new ImageIcon("resources/images/matrix_poster.jpg"));
		lblMovie3.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblMovie3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new MediaScreen("The Matrix"));
				thread.start();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblMovie3.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});

		JLabel label = new JLabel();
		label.setIcon(new ImageIcon("resources/images/dracula_poster.jpg"));
		label.setBorder(new LineBorder(new Color(0, 0, 0)));
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new MediaScreen("Dracula"));
				thread.start();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(19)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE).addGap(18)
						.addComponent(lblMovie2, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE).addGap(18)
						.addComponent(lblMovie3, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(17, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(25)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(label, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMovie3, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMovie2, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(28, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);

		JRadioButton rdbtnMovies = new JRadioButton("Movies");
		rdbtnMovies.setFont(new Font("SansSerif", Font.PLAIN, 20));
		ViewPaneButtonGroup.add(rdbtnMovies);
		rdbtnMovies.setForeground(Color.WHITE);
		rdbtnMovies.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {

				Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
				List<Map<String, Object>> results = DBMS.queryDatabase(conn,
						"SELECT * FROM GlobalMedia WHERE mediatype='Movie'", new String[] {});
				DBMS.closeConnection(conn);

				populate_list.clear();
				for (Map<String, Object> entry : results) {
					populate_list.addElement(entry.get("mediatitle").toString());
				}
			}

		});

		JRadioButton rdbtnTv = new JRadioButton("TV");
		rdbtnTv.setFont(new Font("SansSerif", Font.PLAIN, 20));
		ViewPaneButtonGroup.add(rdbtnTv);
		rdbtnTv.setForeground(Color.WHITE);
		rdbtnTv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {

				Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
				List<Map<String, Object>> results = DBMS.queryDatabase(conn,
						"SELECT * FROM GlobalMedia WHERE mediatype='TV Show'", new String[] {});
				DBMS.closeConnection(conn);

				populate_list.clear();
				for (Map<String, Object> entry : results) {
					populate_list.addElement(entry.get("mediatitle").toString());
				}
			}

		});

		JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.setFont(new Font("SansSerif", Font.PLAIN, 20));
		ViewPaneButtonGroup.add(rdbtnAll);
		rdbtnAll.setSelected(true);
		rdbtnAll.setForeground(Color.WHITE);
		rdbtnAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {

				Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
				List<Map<String, Object>> results = DBMS.queryDatabase(conn, "SELECT * FROM GlobalMedia",
						new String[] {});
				DBMS.closeConnection(conn);

				populate_list.clear();
				for (Map<String, Object> entry : results) {
					populate_list.addElement(entry.get("mediatitle").toString());
				}
			}

		});

		JLabel lblIlirsFace = new JLabel();

		lblIlirsFace.setForeground(Color.WHITE);
		lblIlirsFace.setBackground(Color.WHITE);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Connection conn = DBMS.establishServerDatabaseConnection("csc301");
				String query = "SELECT * FROM GlobalMedia WHERE 1=1";
				if (rdbtnTv.isSelected()) {
					query += " AND mediatype='TV Show'";
				} else if (rdbtnMovies.isSelected()) {
					query += " AND mediatype='Movie'";
				}
				List<Map<String, Object>> results = DBMS.queryDatabase(conn, query + ";", new String[] {});
				String searchMovie = txtSearchForMovies.getText().trim().toLowerCase();
				DBMS.closeConnection(conn);

				populate_list.clear();
				for (Map<String, Object> entry : results) {
					String title = entry.get("mediatitle").toString().trim().toLowerCase();
					if (title.startsWith(searchMovie)) {
						populate_list.addElement(entry.get("mediatitle").toString());
					}
				}
			}

		});

		JLabel lblLogo = new JLabel("");
		lblLogo.setIcon(new ImageIcon("resources/images/ilir_logo.png"));

		Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
		List<Map<String, Object>> results = DBMS.queryDatabase(conn, "SELECT * FROM GlobalMedia", new String[] {});
		DBMS.closeConnection(conn);

		for (Map<String, Object> result : results) {
			populate_list.addElement((String) result.get("mediatitle"));
		}

		JLst = new JList(populate_list);

		JLst.setBackground(Color.LIGHT_GRAY);
		JLst.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		JLst.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Thread thread = new Thread(new MediaScreen((String) JLst.getSelectedValue()));
					thread.start();
				}
			}

		});

		JScrollPane JScrollPaneViewContainer = new JScrollPane(JLst);
		JScrollPaneViewContainer.setBackground(Color.LIGHT_GRAY);
		JScrollPaneViewContainer.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addContainerGap(52, Short.MAX_VALUE)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblIlirsFace))
								.addGap(43))
						.addGroup(groupLayout.createSequentialGroup().addGap(237).addComponent(lblIlirsPicks)
								.addPreferredGap(ComponentPlacement.RELATED)))
						.addGroup(
								groupLayout.createSequentialGroup().addContainerGap()
										.addComponent(lblLogo, GroupLayout.PREFERRED_SIZE, 273,
												GroupLayout.PREFERRED_SIZE)
										.addGap(144)))
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addComponent(txtSearchForMovies, 239, 239, 239)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSearch)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnNewButton,
										GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(JScrollPaneViewContainer, GroupLayout.PREFERRED_SIZE, 634,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createSequentialGroup()
												.addComponent(rdbtnAll, GroupLayout.PREFERRED_SIZE, 46,
														GroupLayout.PREFERRED_SIZE)
												.addGap(18)
												.addComponent(rdbtnTv, GroupLayout.PREFERRED_SIZE, 57,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(rdbtnMovies)))))
				.addContainerGap(10, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
						.createSequentialGroup().addGap(42)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSearchForMovies, GroupLayout.PREFERRED_SIZE, 40,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addGap(8)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(rdbtnAll)
								.addComponent(rdbtnTv).addComponent(rdbtnMovies))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(JScrollPaneViewContainer,
								GroupLayout.PREFERRED_SIZE, 584, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addComponent(lblIlirsFace).addGap(34)
								.addComponent(lblLogo, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
								.addGap(47).addComponent(lblIlirsPicks).addGap(18)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)))
				.addGap(14)));
		setLayout(groupLayout);
	}

	@Override
	public void updateAspects() {
	}
}
