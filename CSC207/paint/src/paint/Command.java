package ca.utoronto.utm.paint;

public interface Command {
    public abstract void execute(String ins, Point p);
}
