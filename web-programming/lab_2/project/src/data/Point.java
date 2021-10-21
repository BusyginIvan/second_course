package data;

import java.io.Serializable;

public class Point implements Serializable {
    private final double x, y, r;
    private final boolean result;

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        result = checkCoordinates(x, y, r);
    }

    private boolean checkCoordinates(double x, double y, double r) {
        return  (x >= 0) && (x <= r/2) && (y >= -r) && (y <= 0) ||
                (x <= 0) && (y >= 0) && (y <= (r + x)/2) ||
                (x*x + y*y <= r*r/4) && (x <= 0) && (y <= 0);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
    public boolean isResult() { return result; }
}