package ca.utoronto.utm.paint;

import java.awt.Graphics2D;

public class Rectangle implements Shape {
    private Point edge;
    private int width, height;

    public Rectangle(Point x, int width, int height) {
        this.edge = x;
        this.width = width;
        this.height = height;
    }

    public Point getEdge() {
        return edge;
    }

    public void setEdge(Point x) {
        this.edge = x;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawRect(this.edge.getX(), this.edge.getY(), this.width, this.height);
    }
}
