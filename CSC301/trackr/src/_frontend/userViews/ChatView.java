package _frontend.userViews;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import _backend.framework.controllers.UserManager;
import _backend.utils.chatService.ReadOnlyThread;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

/**
 * NOTE THIS WILL CRASH NOT BEING ABLE TO CONNECT SINCE I HOST THE SERVER
 * LOCALLY
 */

public class ChatView extends JFrame implements Runnable {
	public static String FAIZ_IP = "142.1.7.137";
	public static String user = "Michelle";

	private String userTo;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private JTextField txtClickHereTo;
	private JTextArea outputText;

	public ChatView(String userTo) {
		setTitle("Chat with /Friend's Username/");
		getContentPane().setBackground(Color.BLACK);
		this.userTo = userTo;

		txtClickHereTo = new JTextField();
		txtClickHereTo.setText("Click here to send a message. . .");
		txtClickHereTo.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String message = txtClickHereTo.getText();

				out.println(message);
				txtClickHereTo.setText("");

			}

		});

		outputText = new JTextArea();
		outputText.setBackground(Color.LIGHT_GRAY);
		outputText.setBorder(new LineBorder(Color.DARK_GRAY, 5));
		outputText.setEditable(false);
		outputText.setRows(10);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(outputText, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(txtClickHereTo, GroupLayout.PREFERRED_SIZE, 381,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(outputText, GroupLayout.PREFERRED_SIZE, 396, GroupLayout.PREFERRED_SIZE).addGap(7)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtClickHereTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSend))
						.addContainerGap(16, Short.MAX_VALUE)));
		getContentPane().setLayout(groupLayout);
		this.setupScreen();
		this.setupSocket();
		this.setVisible(true);
	}

	private void setupSocket() {
		final String host = FAIZ_IP;
		final int portNumber = 4444;
		try {
			this.socket = new Socket(host, portNumber);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);

			// Temp register
			this.out.println(UserManager.getInstance().username());
			this.out.println(userTo);

			// this is where I would setup who we are talking to

		} catch (Exception e) {
			System.out.println("Could Not Connect to Server, It may be offline");
			System.exit(1);
		}
	}

	private void setupScreen() {
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void run() {
		System.out.println(this.in);
		System.out.println(this.out);
		// Make a new thread just for reading
		Thread readThread = new Thread(new ReadOnlyThread(this.in, this.outputText));
		readThread.start();

	}
}
