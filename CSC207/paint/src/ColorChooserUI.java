import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ColorChooserUI extends JFrame {
    private JButton b;
    private Color color = (Color.WHITE);
    private JPanel p;

    public ColorChooserUI() {
        super("Color Chooser");
        p = new JPanel();
        p.setBackground(color);

        b = new JButton("Choose a color");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                color = JColorChooser.showDialog(null, "Pick your color", color);
                if (color == null)
                    color = (Color.WHITE);

                p.setBackground(color);
            }
        });

        add(p, BorderLayout.CENTER);
        add(b, BorderLayout.SOUTH);
        setSize(425, 150);
        setVisible(true);
    }

    public static void main(String[] args) {
        ColorChooserUI c = new ColorChooserUI();
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
