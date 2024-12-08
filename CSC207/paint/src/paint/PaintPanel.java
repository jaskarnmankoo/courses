package ca.utoronto.utm.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

// https://docs.oracle.com/javase/8/docs/api/java/awt/Graphics2D.html
// https://docs.oracle.com/javase/tutorial/2d/

class PaintPanel extends JPanel implements Observer, MouseMotionListener, MouseListener {
    /**
     *
     */
    private static final long serialVersionUID = 1 L;
    private int i = 0;
    private PaintModel model; // slight departure from MVC, because of the way painting works
    private View view; // So we can talk to our parent or other components of the view
    private PaintConcreteCommand pcc; // to make up all the commands
    private String mode; // modifies how we interpret input (could be better?)
    private Circle circle; // the circle we are building
    private Color color;

    public PaintPanel(PaintModel model, View view) {
        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(300, 300));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.mode = "Circle"; // bad code here?

        this.model = model;
        this.model.addObserver(this);
        this.pcc = new PaintConcreteCommand(this.model);
        this.view = view;
    }

    /**
     *  View aspect of this
     */
    public void paintComponent(Graphics g) {
        // Use g to draw on the JPanel, lookup java.awt.Graphics in
        // the javadoc to see more of what this can do for you!!

        super.paintComponent(g); //paint background
        Graphics2D g2d = (Graphics2D) g; // lets use the advanced api
        // setBackground (Color.blue);
        // Origin is at the top left of the window 50 over, 75 down
        g2d.setColor(Color.black);

        // Draw Shapes ADD MORE METHODS TO SHAPE INTERFACE
        ArrayList<Shape> shapes = this.model.getShapes();
        for (Shape s: shapes) {
            s.draw(g2d);
        }

        g2d.dispose();
    }

    @Override
    public void update(Observable o, Object arg) {
        // Not exactly how MVC works, but similar.
        this.repaint(); // Schedule a call to paintComponent
    }

    /**
     *  Controller aspect of this
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // MouseMotionListener below
    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        pcc.sendCommand(mode, "move", p);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        pcc.sendCommand(mode, "drag", p);
    }

    // MouseListener below
    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        pcc.sendCommand(mode, "click", p);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        pcc.sendCommand(mode, "press", p);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        pcc.sendCommand(mode, "release", p);

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        pcc.sendCommand(mode, "enter", p);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());
        pcc.sendCommand(mode, "exit", p);
    }
}
