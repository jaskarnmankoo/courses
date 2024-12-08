package ca.utoronto.utm.paint;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Line implements Shape {
    private ArrayList<Point> points = new ArrayList<Point>();

    public Line(Point p1) {
        this.points.add(p1);
    }

    public ArrayList<Point> getPoints() {
        return this.points;
    }

    public void addPoint(Point p) {
        this.points.add(p);
    }

    @Override
    public void draw(Graphics2D g2d) {
        for (int i = 0; i < this.points.size() - 1; i++) {
            Point p1 = this.points.get(i);
            Point p2 = this.points.get(i + 1);
            g2d.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }
}
