package ca.utoronto.utm.paint;

public class RectangleCommand implements Command {
    private PaintModel m;
    private Rectangle rectangle;
    private int width;
    private int height;
    private Point origin;

    public RectangleCommand(PaintModel m) {
        this.m = m;
    }

    @Override
    public void execute(String ins, Point p) {
        if (ins.equals("drag")) {
            this.height = Math.abs(p.getY() - this.origin.getY());
            this.width = Math.abs(p.getX() - this.origin.getX());
            Point current = this.rectangle.getEdge();

            if (p.getY() < this.origin.getY()) {
                current.setY(p.getY());
            } else {
                current.setY(origin.getY());
            }

            if (p.getX() < this.origin.getX()) {
                current.setX(p.getX());
            } else {
                current.setX(origin.getX());
            }

            this.rectangle.setHeight(height);
            this.rectangle.setWidth(width);
            this.m.update();
        }

        if (ins.equals("move")) {
        }

        if (ins.equals("click")) {
        }

        if (ins.equals("press")) {
            Point edge = new Point(p.getX(), p.getY());
            this.origin = new Point(p.getX(), p.getY());
            this.width = 0;
            this.height = 0;
            this.rectangle = new Rectangle(edge, 0, 0);
            this.m.addShape(this.rectangle);
        }

        if (ins.equals("release")) {
            if (this.rectangle != null) {
                this.height = Math.abs(p.getY() - this.origin.getY());
                this.width = Math.abs(p.getX() - this.origin.getX());
                Point current = this.rectangle.getEdge();

                if (p.getY() < this.origin.getY()) {
                    current.setY(p.getY());
                } else {
                    current.setY(origin.getY());
                }

                if (p.getX() < this.origin.getX()) {
                    current.setX(p.getX());
                } else {
                    current.setX(origin.getX());
                }

                this.rectangle.setHeight(height);
                this.rectangle.setWidth(width);
                this.m.update();
                this.rectangle = null;
            }
        }

        if (ins.equals("enter")) {
        }

        if (ins.equals("exit")) {
        }
    }
}
