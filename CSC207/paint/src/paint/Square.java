package ca.utoronto.utm.paint;

import java.awt.Graphics2D;

public class Square extends Rectangle implements Shape {
    public Square(Point x, int height, int width) {
        super(x, height, width);
    }

    public void draw(Graphics2D g2d) {
        super.draw(g2d);
    }
}
