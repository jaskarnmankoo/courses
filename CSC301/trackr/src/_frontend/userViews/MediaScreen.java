package _frontend.userViews;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import _backend.framework.controllers.UserManager;
import _backend.framework.models.user.UserMedia;
import _backend.utils.database.DBMS;

@SuppressWarnings("serial")
public class MediaScreen extends JFrame implements Runnable {
	private UserMedia currentMedia;

	public MediaScreen(String mediaTitle) {
		super("Trackr - Media Screen");
		getContentPane().setBackground(Color.BLACK);
		setForeground(Color.BLACK);

		// Temp
		Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
		List<Map<String, Object>> results = DBMS.queryDatabase(conn, "SELECT * FROM GlobalMedia WHERE mediatitle=?",
				new String[] { mediaTitle });
		if (results.size() == 0) {
			DBMS.closeConnection(conn);
			System.exit(1);
		}

		Map<String, Object> info = results.get(0);
		DBMS.closeConnection(conn);

		initialize();
		JPanel panel = new JPanel();
		panel.setBounds(13, 63, 635, 198);
		panel.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		panel.setBackground(Color.LIGHT_GRAY);

		JLabel lblNameOfMedia = new JLabel((String) info.get("mediatitle"));
		lblNameOfMedia.setBounds(13, 14, 500, 43);
		lblNameOfMedia.setForeground(Color.WHITE);
		lblNameOfMedia.setFont(new Font("Modern No. 20", Font.PLAIN, 56));

		JLabel lblCoverArt = new JLabel("Cover Art goes here");
		lblCoverArt.setBounds(5, 5, 158, 188);
		lblCoverArt.setBorder(new LineBorder(new Color(0, 0, 0)));

		JLabel lblOverallUserRating = new JLabel("Genre: " + (String) info.get("mediagenre"));
		lblOverallUserRating.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblOverallUserRating.setBounds(169, 34, 164, 16);

		JLabel lblStatusAiringOr = new JLabel("Status: N/A");
		lblStatusAiringOr.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblStatusAiringOr.setBounds(169, 61, 218, 21);

		JLabel lblNumberOfEpisodes = new JLabel("Number of Episodes: " + (String) info.get("episodecount"));
		lblNumberOfEpisodes.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblNumberOfEpisodes.setBounds(169, 89, 294, 16);

		JLabel lblLength = new JLabel("Run Time: " + (String) info.get("mediaduration"));
		lblLength.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblLength.setBounds(169, 117, 178, 16);

		JButton btnlike = new JButton("Add to Public List");
		btnlike.setBounds(169, 161, 164, 28);
		btnlike.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Connection conn = DBMS.establishLocalDatabaseConnection(UserManager.getInstance().username());
				DBMS.updateDatabase(conn, "INSERT INTO PublicList VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new String[] { (String) info.get("mediatitle"), (String) info.get("mediatype"),
								(String) info.get("mediagenre"), (String) info.get("mediaduration"), "N/A", "0",
								"00000000", "00000000", "0" });

				DBMS.closeConnection(conn);

				conn = DBMS.establishServerDatabaseConnection(UserManager.getInstance().username().toLowerCase());

				if (conn == null) {
					return;
				}

				DBMS.updateDatabase(conn, "INSERT INTO PublicList VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new String[] { (String) info.get("mediatitle"), (String) info.get("mediatype"),
								(String) info.get("mediagenre"), (String) info.get("mediaduration"), "N/A", "0",
								"00000000", "00000000", "0" });

				DBMS.closeConnection(conn);

			}

		});

		JButton btnNewButton = new JButton("Post Review");
		btnNewButton.setBounds(13, 646, 168, 51);

		JSlider slider = new JSlider();
		slider.setMaximum(5);
		slider.setMinimum(0);
		slider.setBounds(187, 662, 200, 21);

		Connection reviewConn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
		String reviewQuery = "SELECT * FROM Ratings WHERE mediatitle = ? ORDER BY RANDOM() LIMIT 1";

		JTextArea txtrOtherPeoplesReviews = new JTextArea();
		txtrOtherPeoplesReviews.setBounds(13, 314, 636, 136);
		txtrOtherPeoplesReviews.setText("No Reviews Found");
		txtrOtherPeoplesReviews.setEditable(false);
		txtrOtherPeoplesReviews.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		txtrOtherPeoplesReviews.setBackground(Color.LIGHT_GRAY);

		List<Map<String, Object>> reviewResults = DBMS.queryDatabase(reviewConn, reviewQuery,
				new String[] { mediaTitle });
		if (!reviewResults.isEmpty()) {
			String reviewerName = (String) reviewResults.get(0).get("username");
			int reviewRating = Integer.parseInt((String) reviewResults.get(0).get("rating"));
			String reviewText = (String) reviewResults.get(0).get("review");
			String finalReview = "Username: " + reviewerName + " (" + reviewRating + ")\n" + reviewText;
			txtrOtherPeoplesReviews.setText(finalReview);
		}

		JTextArea txtrYourReviewGoes = new JTextArea();
		txtrYourReviewGoes.setBounds(13, 498, 636, 136);
		txtrYourReviewGoes.setText("Your review goes here");
		txtrYourReviewGoes.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		txtrYourReviewGoes.setBackground(Color.LIGHT_GRAY);
		getContentPane().setLayout(null);
		getContentPane().add(btnNewButton);
		getContentPane().add(slider);
		getContentPane().add(txtrOtherPeoplesReviews);
		getContentPane().add(panel);
		panel.setLayout(null);
		panel.add(lblCoverArt);
		panel.add(btnlike);
		panel.add(lblLength);
		panel.add(lblOverallUserRating);
		panel.add(lblStatusAiringOr);
		panel.add(lblNumberOfEpisodes);

		JButton btnAddToPrivate = new JButton("Add to Private List");
		btnAddToPrivate.setBounds(334, 161, 164, 28);
		btnAddToPrivate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				Connection conn = DBMS.establishLocalDatabaseConnection(UserManager.getInstance().username());
				DBMS.updateDatabase(conn, "INSERT INTO PrivateList VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new String[] { (String) info.get("mediatitle"), (String) info.get("mediatype"),
								(String) info.get("mediagenre"), (String) info.get("mediaduration"), "N/A", "0",
								"00000000", "00000000", "0" });

				DBMS.closeConnection(conn);

				conn = DBMS.establishServerDatabaseConnection(UserManager.getInstance().username().toLowerCase());

				if (conn == null) {
					return;
				}

				DBMS.updateDatabase(conn, "INSERT INTO PrivateList VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new String[] { (String) info.get("mediatitle"), (String) info.get("mediatype"),
								(String) info.get("mediagenre"), (String) info.get("mediaduration"), "N/A", "0",
								"00000000", "00000000", "0" });

				DBMS.closeConnection(conn);

			}

		});
		panel.add(btnAddToPrivate);
		getContentPane().add(lblNameOfMedia);
		getContentPane().add(txtrYourReviewGoes);
		initialize();

		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Connection conn = DBMS.establishServerDatabaseConnection("csc301");
				Connection conn2 = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
				// SHOULD CHECK IF IN DATABASE FIRST

				DBMS.updateDatabase(conn, "INSERT INTO Ratings VALUES(?, ?, ?, ?)",
						new String[] { (String) info.get("mediatitle"), UserManager.getInstance().username(),
								txtrYourReviewGoes.getText(), Integer.toString(slider.getValue()) });

				DBMS.updateDatabase(conn2, "INSERT INTO Ratings VALUES(?, ?, ?, ?)",
						new String[] { (String) info.get("mediatitle"), UserManager.getInstance().username(),
								txtrYourReviewGoes.getText(), Integer.toString(slider.getValue()) });

				DBMS.closeConnection(conn);
				DBMS.closeConnection(conn2);
			}
		});

		this.setVisible(true);
	}

	public void initialize() {
		this.setBackground(Color.BLACK);
		this.setSize(680, 760);
	}

	@Override
	public void run() {
	}
}
