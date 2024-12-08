package ca.utoronto.utm.paint;

public class SquiggleCommand implements Command {
    private PaintModel m;
    private Squiggle line;

    public SquiggleCommand(PaintModel m) {
        this.m = m;
    }

    @Override
    public void execute(String ins, Point p) {
        if (ins.equals("drag")) {
            Point edge = new Point(p.getX(), p.getY());
            this.line.addPoint((edge));
            this.m.update();
        }

        if (ins.equals("move")) {
        }

        if (ins.equals("click")) {
        }

        if (ins.equals("press")) {
            Point edge = new Point(p.getX(), p.getY());
            this.line = new Squiggle(edge);
            this.m.addShape(this.line);
        }

        if (ins.equals("release")) {
            if (this.line != null) {
                this.m.update();
                this.line = null;
            }
        }

        if (ins.equals("enter")) {
        }

        if (ins.equals("exit")) {
        }
    }
}
