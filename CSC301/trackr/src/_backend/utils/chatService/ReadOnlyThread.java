package _backend.utils.chatService;

import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JTextArea;

/**
 * This is run on a separate thread (process) with the sole purpose of reading
 * infinitely and writing all data it reads to the screen for the user to read.
 */
public class ReadOnlyThread implements Runnable {
	private BufferedReader inStream;
	private JTextArea output;

	public ReadOnlyThread(BufferedReader inStream, JTextArea output) {
		this.inStream = inStream;
		this.output = output;
	}

	/**
	 * This reads a message and appends it to outbox consistently
	 */
	@Override
	public void run() {

		while (true) {
			try {
				String message = this.inStream.readLine();
				this.output.append(message + "\n");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}
}
