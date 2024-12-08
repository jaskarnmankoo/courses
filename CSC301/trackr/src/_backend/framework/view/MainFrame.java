package _backend.framework.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import _backend.utils.database.UserFileManager;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    public MainFrame() {
        super("Ilist Demedia");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ViewContainer container = ViewContainer.getInstance(); // ViewState Manager

        this.setContentPane(container);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        // Setting up the local Database if the user needs it
        UserFileManager.initializeUserFiles();

        // Syncing with the Server
        UserFileManager.syncWithServer();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainFrame();
            }

        });
    }
}
