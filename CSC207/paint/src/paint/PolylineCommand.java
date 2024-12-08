package ca.utoronto.utm.paint;

public class PolylineCommand implements Command {
    private PaintModel m;
    private Line line;
    private Point edge;

    public PolylineCommand(PaintModel m) {
        this.m = m;
    }

    @Override
    public void execute(String ins, Point p) {
        if (ins.equals("drag")) {
        }

        if (ins.equals("move")) {
        }

        if (ins.equals("click")) {
        }

        if (ins.equals("press")) {
            if (this.line != null) {
                Point current = new Point(p.getX(), p.getY());
                this.line.addPoint(current);
                this.m.update();
                this.edge = current;
            } else {
                this.edge = new Point(p.getX(), p.getY());
                this.line = new Line(edge);
                this.m.addShape(this.line);
            }
        }

        if (ins.equals("release")) {
            if (this.line != null) {
                Point current = new Point(p.getX(), p.getY());
                this.line.addPoint(current);
                this.m.update();
                this.edge = current;
            }
        }

        if (ins.equals("enter")) {
        }

        if (ins.equals("exit")) {
        }
    }
}
