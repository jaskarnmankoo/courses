package ca.utoronto.utm.paint;

public class CircleCommand implements Command {
    private PaintModel m;
    private Circle circle;
    private int r;

    public CircleCommand(PaintModel m) {
        this.m = m;
    }

    @Override
    public void execute(String ins, Point p) {

        if (ins.equals("drag")) {
            int x = Math.abs(this.circle.getCentre().getX() - p.getX());
            int y = Math.abs(this.circle.getCentre().getY() - p.getY());
            this.r = (int) Math.sqrt((x * x) + (y * y));
            this.circle.setRadius(r);
            this.m.update();
        }

        if (ins.equals("move")) {
        }

        if (ins.equals("click")) {
        }

        if (ins.equals("press")) {
            Point centre = new Point(p.getX(), p.getY());
            this.r = 0;
            this.circle = new Circle(centre, this.r);
            this.m.addShape(this.circle);
        }

        if (ins.equals("release")) {
            if (this.circle != null) {
                int x = Math.abs(this.circle.getCentre().getX() - p.getX());
                int y = Math.abs(this.circle.getCentre().getY() - p.getY());
                this.r = (int) Math.sqrt((x * x) + (y * y));
                this.circle.setRadius(r);
                this.circle = null;
            }
        }

        if (ins.equals("enter")) {
        }

        if (ins.equals("exit")) {
        }
    }
}
