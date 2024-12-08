package ca.utoronto.utm.paint;

public class PaintConcreteCommand {
    private PaintModel m;
    private CircleCommand c;
    private SquiggleCommand s;
    private RectangleCommand r;
    private SquareCommand sq;
    private PolylineCommand pl;

    public PaintConcreteCommand(PaintModel m) {
        this.m = m;
        createCommands();
    }

    public void createCommands() {
        c = new CircleCommand(this.m);
        s = new SquiggleCommand(this.m);
        r = new RectangleCommand(this.m);
        sq = new SquareCommand(this.m);
        pl = new PolylineCommand(this.m);
    }

    public void sendCommand(String cmd, String ins, Point p) {
        if (cmd.equals("Circle")) {
            c.execute(ins, p);
        }

        if (cmd.equals("Squiggle")) {
            s.execute(ins, p);
        }

        if (cmd.equals("Rectangle")) {
            r.execute(ins, p);
        }

        if (cmd.equals("Square")) {
            sq.execute(ins, p);
        }

        if (cmd.equals("Polyline")) {
            pl.execute(ins, p);
        }
    }
}
