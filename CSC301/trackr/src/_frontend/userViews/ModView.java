package _frontend.userViews;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import _backend.framework.controllers.UserManager;
import _backend.framework.models.media.PMedia;
import _backend.utils.database.DBMS;
import _backend.utils.database.MediaHashMap;

public class ModView extends JFrame implements Runnable {
	private JTextField textFieldWatched;
	private PMedia media;
	private int which;
	private String mediaName;
	public static final int FROM_PUBLIC = 0, FROM_PRIVATE = 1;

	/**
	 * Create the application.
	 */
	public ModView(String mediaName, int which) {
		super("Updating " + mediaName);
		getContentPane().setBackground(Color.BLACK);
		MediaHashMap map = null;
		this.mediaName = mediaName;
		this.which = which;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBackground(Color.BLACK);
		this.setSize(880, 739);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		String[] userLists = { "PublicList", "PrivateList" };

		// Get the media we want to look at
		PMedia currMedia = new PMedia();
		currMedia.setTitle(this.mediaName);

		JLabel lblAddMediaTo = new JLabel("Update Progress");
		lblAddMediaTo.setBounds(6, 0, 386, 58);
		lblAddMediaTo.setForeground(Color.WHITE);
		lblAddMediaTo.setFont(new Font("Modern No. 20", Font.PLAIN, 56));

		JLabel lblMediaTitle = new JLabel("Name:");
		lblMediaTitle.setBounds(41, 118, 142, 32);
		lblMediaTitle.setForeground(Color.WHITE);
		lblMediaTitle.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JLabel lblStatus = new JLabel("Status: ");
		lblStatus.setBounds(41, 203, 142, 32);
		lblStatus.setForeground(Color.WHITE);
		lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 24));

		// Set text of status
		// lblStatus.setText(currMedia.getStatus());

		JLabel lblEpisodesWatched = new JLabel("Episodes Watched: ");
		lblEpisodesWatched.setBounds(41, 281, 211, 32);
		lblEpisodesWatched.setForeground(Color.WHITE);
		lblEpisodesWatched.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JLabel lblYourScore = new JLabel("Your Score: ");
		lblYourScore.setBounds(41, 370, 142, 32);
		lblYourScore.setForeground(Color.WHITE);
		lblYourScore.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JLabel lblStartDate = new JLabel("Start Date: ");
		lblStartDate.setBounds(41, 454, 142, 32);
		lblStartDate.setForeground(Color.WHITE);
		lblStartDate.setFont(new Font("SansSerif", Font.PLAIN, 24));

		// Set text of start date
		// lblStartDate.setText(currMedia.getStartDate());

		JLabel lblFinishDate = new JLabel("Finish Date: ");
		lblFinishDate.setBounds(41, 539, 142, 32);
		lblFinishDate.setForeground(Color.WHITE);
		lblFinishDate.setFont(new Font("SansSerif", Font.PLAIN, 24));

		// Set text of end date

		String[] statusStrings = { "Watching", "Completed", "Want to Watch" };
		JComboBox<String> comboBoxStatus = new JComboBox<String>(statusStrings);
		comboBoxStatus.setBounds(279, 198, 193, 42);
		comboBoxStatus.setFont(new Font("SansSerif", Font.PLAIN, 24));

		// Change the currMedia's status based on the combobox
		comboBoxStatus.setSelectedItem("Watching");

		textFieldWatched = new JTextField();
		textFieldWatched.setBounds(279, 269, 212, 44);
		textFieldWatched.setFont(new Font("SansSerif", Font.PLAIN, 24));
		textFieldWatched.setColumns(10);

		JLabel label = new JLabel("/5");
		label.setBounds(477, 371, 53, 32);
		label.setFont(new Font("SansSerif", Font.PLAIN, 24));

		String[] scoreStrings = { "0", "1", "2", "3", "4", "5" };
		JComboBox<String> comboBoxScore = new JComboBox<String>(scoreStrings);
		comboBoxScore.setBounds(279, 365, 132, 42);
		comboBoxScore.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JLabel lblMonthSD = new JLabel("Month:");
		lblMonthSD.setForeground(Color.WHITE);
		lblMonthSD.setBounds(181, 454, 85, 32);
		lblMonthSD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JLabel lblDaySD = new JLabel("Day:");
		lblDaySD.setBounds(441, 454, 85, 32);
		lblDaySD.setForeground(Color.WHITE);
		lblDaySD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JLabel lblYearSD = new JLabel("Year:");
		lblYearSD.setBounds(633, 454, 85, 32);
		lblYearSD.setForeground(Color.WHITE);
		lblYearSD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JLabel label_1 = new JLabel("Month:");
		label_1.setForeground(Color.WHITE);
		label_1.setBounds(181, 539, 85, 32);
		label_1.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JLabel lblDayFD = new JLabel("Day:");
		lblDayFD.setBounds(441, 539, 85, 32);
		lblDayFD.setForeground(Color.WHITE);
		lblDayFD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JLabel lblYearFD = new JLabel("Year:");
		lblYearFD.setBounds(633, 539, 85, 32);
		lblYearFD.setForeground(Color.WHITE);
		lblYearFD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		String[] monthStrings = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		JComboBox<String> comboBoxMonthSD = new JComboBox<String>(monthStrings);
		comboBoxMonthSD.setBounds(279, 449, 150, 42);
		comboBoxMonthSD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JComboBox<String> comboBoxMonthFD = new JComboBox<String>(monthStrings);
		comboBoxMonthFD.setBounds(279, 534, 150, 42);
		comboBoxMonthFD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		String[] dayStrings = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };

		JComboBox<String> comboBoxDaySD = new JComboBox<String>(dayStrings);
		comboBoxDaySD.setBounds(511, 449, 110, 42);
		comboBoxDaySD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JComboBox<String> comboBoxDayFD = new JComboBox<String>(dayStrings);
		comboBoxDayFD.setBounds(511, 534, 110, 42);
		comboBoxDayFD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		String[] yearStrings = { "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990",
				"1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003",
				"2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016",
				"2017", "2018" };

		JComboBox<String> comboBoxYearSD = new JComboBox<String>(yearStrings);
		comboBoxYearSD.setBounds(702, 449, 95, 42);
		comboBoxYearSD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		JComboBox<String> comboBoxYearFD = new JComboBox<String>(yearStrings);
		comboBoxYearFD.setBounds(702, 534, 95, 42);
		comboBoxYearFD.setFont(new Font("SansSerif", Font.PLAIN, 24));

		// Parse all the dates together

		// Set the currMedia's new items if need be

		// May or may not work

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(41, 648, 134, 41);
		btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Get the date we entered
				String startDateMonth = (String) comboBoxMonthSD.getSelectedItem();
				String finDateMonth = (String) comboBoxMonthFD.getSelectedItem();

				String startDateDay = (String) comboBoxDaySD.getSelectedItem();
				String finDateDay = (String) comboBoxDayFD.getSelectedItem();

				String startDateYear = (String) comboBoxYearSD.getSelectedItem();
				String finDateYear = (String) comboBoxYearFD.getSelectedItem();

				String startDateFinal = startDateYear + startDateMonth + startDateDay;
				String finDateFinal = finDateYear + finDateMonth + finDateDay;

				currMedia.setTitle(mediaName);
				currMedia.setStatus((String) comboBoxStatus.getSelectedItem());
				currMedia.setStartDate(startDateFinal);
				currMedia.setEndDate(finDateFinal);
				currMedia.setEpisodeCount(Integer.parseInt(textFieldWatched.getText()));

				Connection conn = DBMS.establishLocalDatabaseConnection(UserManager.getInstance().username());
				Connection conn2 = DBMS
						.establishServerDatabaseConnection(UserManager.getInstance().username().toLowerCase());

				String updateQuery = "UPDATE " + userLists[which]
						+ " SET status = ?, episodecount = ?, startdate = ?, enddate = ?, userrating = ? WHERE mediatitle = ?";

				String args[] = { currMedia.getStatus(), Integer.toString(currMedia.getEpisodeCount()),
						currMedia.getStartDate(), currMedia.getEndDate(), (String) comboBoxScore.getSelectedItem(),
						currMedia.getTitle() };

				DBMS.updateDatabase(conn, updateQuery, args);

				if (conn2 == null) {
					DBMS.closeConnection(conn);
					return;
				}

				DBMS.updateDatabase(conn2, updateQuery, args);

				DBMS.closeConnection(conn);
				DBMS.closeConnection(conn2);
			}

		});

		JLabel lblNameOfMedia = new JLabel(this.mediaName);
		lblNameOfMedia.setBounds(279, 118, 253, 29);
		lblNameOfMedia.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNameOfMedia.setForeground(Color.WHITE);

		Connection conn = DBMS.establishLocalDatabaseConnection(UserManager.getInstance().username());
		List<Map<String, Object>> result = DBMS.queryDatabase(conn,
				"SELECT * FROM " + userLists[this.which] + " WHERE mediatitle = ?", new String[] { this.mediaName });
		DBMS.closeConnection(conn);

		System.out.println(result);

		if (result != null && !result.isEmpty()) {
			currMedia.setDuration(Integer.parseInt((String) result.get(0).get("mediaduration")));
			currMedia.setEpisodeCount(Integer.parseInt((String) result.get(0).get("episodecount")));
			currMedia.setStartDate((String) result.get(0).get("startdate"));
			currMedia.setEndDate((String) result.get(0).get("enddate"));

			textFieldWatched.setText(Integer.toString(currMedia.getEpisodeCount()));
			comboBoxStatus.setSelectedItem(currMedia.getStatus());

			if (currMedia.getStartDate() != null) {
				comboBoxMonthSD.setSelectedItem(currMedia.getStartDate().substring(4, 6));
				comboBoxDaySD.setSelectedItem(currMedia.getStartDate().substring(6, 8));
				comboBoxYearSD.setSelectedItem(currMedia.getStartDate().substring(0, 4));
			}

			if (currMedia.getEndDate() != null) {
				comboBoxMonthFD.setSelectedItem(currMedia.getEndDate().substring(4, 6));
				comboBoxDayFD.setSelectedItem(currMedia.getEndDate().substring(6, 8));
				comboBoxYearFD.setSelectedItem(currMedia.getEndDate().substring(0, 4));
			}

			comboBoxScore.setSelectedItem((String) result.get(0).get("userrating"));
			comboBoxStatus.setSelectedItem((String) result.get(0).get("status"));
		}

		getContentPane().setLayout(null);
		getContentPane().add(lblStatus);
		getContentPane().add(lblEpisodesWatched);
		getContentPane().add(lblFinishDate);
		getContentPane().add(lblStartDate);
		getContentPane().add(lblYourScore);
		getContentPane().add(lblMediaTitle);
		getContentPane().add(lblNameOfMedia);
		getContentPane().add(comboBoxScore);
		getContentPane().add(label);
		getContentPane().add(comboBoxStatus);
		getContentPane().add(textFieldWatched);
		getContentPane().add(lblMonthSD);
		getContentPane().add(comboBoxMonthSD);
		getContentPane().add(label_1);
		getContentPane().add(comboBoxMonthFD);
		getContentPane().add(lblDayFD);
		getContentPane().add(comboBoxDayFD);
		getContentPane().add(lblDaySD);
		getContentPane().add(comboBoxDaySD);
		getContentPane().add(lblYearSD);
		getContentPane().add(comboBoxYearSD);
		getContentPane().add(lblYearFD);
		getContentPane().add(comboBoxYearFD);
		getContentPane().add(btnSubmit);
		getContentPane().add(lblAddMediaTo);

		setVisible(true);
	}

	@Override
	public void run() {

	}
}
