package ca.utoronto.utm.paint;

public class SquareCommand implements Command {
    private PaintModel m;
    private Square square;
    private int height, width, size;
    private Point origin;

    public SquareCommand(PaintModel m) {
        this.m = m;
    }

    @Override
    public void execute(String ins, Point p) {
        if (ins.equals("drag")) {

            this.height = Math.abs(p.getY() - this.origin.getY());
            this.width = Math.abs(p.getX() - this.origin.getX());
            Point current = this.square.getEdge();
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

            if (this.width < this.height) {
                this.size = this.width;
            } else {
                this.size = this.height;
            }
            this.square.setHeight(this.size);
            this.square.setWidth(this.size);
            this.m.update();
        }

        if (ins.equals("move")) {
        }

        if (ins.equals("click")) {
        }

        if (ins.equals("press")) {
            Point edge = new Point(p.getX(), p.getY());
            this.origin = new Point(p.getX(), p.getY());
            this.square = new Square(edge, 0, 0);
            this.m.addShape(this.square);
        }

        if (ins.equals("release")) {
            if (this.square != null) {
                this.height = Math.abs(p.getY() - this.origin.getY());
                this.width = Math.abs(p.getX() - this.origin.getX());
                Point current = this.square.getEdge();
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

                if (this.width < this.height) {
                    this.size = this.width;
                } else {
                    this.size = this.height;
                }

                this.square.setHeight(this.size);
                this.square.setWidth(this.size);
                this.m.update();
                this.square = null;
            }
        }

        if (ins.equals("enter")) {
        }

        if (ins.equals("exit")) {
        }

    }
}
