package ca.utoronto.utm.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorChooserPanel extends JPanel implements ActionListener {
    private JButton b;
    private Color defaultColor = (Color.WHITE);
    private View view;
    private JColorChooser colorChooser;

    public ColorChooserPanel(View view) {
        this.view = view;
        this.setLayout(new FlowLayout());
        b = new JButton("Color Chooser");
        this.add(b);
        b.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        defaultColor = JColorChooser.showDialog(null, "Pick Your Color", defaultColor);

        if (defaultColor == null) {
            defaultColor = (Color.WHITE);
        }
    }
}
