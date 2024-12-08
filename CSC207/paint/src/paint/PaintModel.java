package ca.utoronto.utm.paint;

import java.util.ArrayList;
import java.util.Observable;

public class PaintModel extends Observable {
    private ArrayList<Shape> shapes = new ArrayList<Shape>();

    public void addShape(Shape s) {
        this.shapes.add(s);
        this.setChanged();
        this.notifyObservers();
    }

    public void update() {
        this.setChanged();
        this.notifyObservers();
    }

    public ArrayList<Shape> getShapes() {
        return this.shapes;
    }
}
