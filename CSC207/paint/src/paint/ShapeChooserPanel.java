package ca.utoronto.utm.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// https://docs.oracle.com/javase/8/docs/api/java/awt/Graphics2D.html
// https://docs.oracle.com/javase/tutorial/2d/

class ShapeChooserPanel extends JPanel implements ActionListener {
    private View view; // So we can talk to our parent or other components of the view

    public ShapeChooserPanel(View view) {
        this.view = view;

        String[] buttonLabels = { "Circle", "Rectangle", "Square", "Squiggle", "Polyline" };

        ImageIcon circle = new ImageIcon("1000px-circle_-_black_simple-s.png");
        ImageIcon rectangle = new ImageIcon("images.png");
        ImageIcon square = new ImageIcon("square.png");
        ImageIcon squiggle = new ImageIcon("img-thing.jpg");
        ImageIcon polyline = new ImageIcon("polylineimg.gif");

        ImageIcon[] iconLabels = { circle, rectangle, square, squiggle, polyline };

        this.setLayout(new GridLayout(buttonLabels.length, 1));

        int i = 0;

        while (i <= 4) {
            JButton button = new JButton(iconLabels[i]);
            button.setActionCommand(buttonLabels[i]);
            this.add(button);
            button.addActionListener(this);
            i++;
        }
    }

    /**
     * Controller aspect of this
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.view.getPaintPanel().setMode(e.getActionCommand());
        System.out.println(e.getActionCommand());
    }
}
