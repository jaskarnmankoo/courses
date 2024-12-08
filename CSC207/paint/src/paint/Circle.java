package ca.utoronto.utm.paint;

import java.awt.Graphics2D;
import java.awt.Color;

public class Circle implements Shape {
    private Point centre;
    private int radius;
    private Color color;

    public Circle(Point centre, int radius) {
        this.centre = centre;
        this.radius = radius;
        this.setColor(color);
    }

    public Point getCentre() {
        return centre;
    }

    public void setCentre(Point centre) {
        this.centre = centre;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawOval(this.centre.getX() - this.radius, this.centre.getY() - this.radius, 2 * this.radius,
                2 * this.radius);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
