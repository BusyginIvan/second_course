package root;

import java.io.Serializable;

public class Point implements Serializable, Cloneable {
    private Float x, y, r = 3f;

    public Point() { }
    public Point(Float x, Float y, Float r) { setX(x); setY(y); setR(r); }

    public Float getX() { return x; }
    public Float getY() { return y; }
    public Float getR() { return r; }

    private Float round(Float a) { return a == null ? null : Math.round(a * 1000) / 1000f; }

    public void setX(Float x) { this.x = round(x); }
    public void setY(Float y) { this.y = round(y); }
    public void setR(Float r) { this.r = round(r); }

    public String getStringResult() {
        if (isValid()) return inArea() ? "да" : "нет";
        return "не определёно";
    }

    public boolean isValid() {
        return initializedFields() && validR(r) && validCoordinate(x) && validCoordinate(y);
    }
    private boolean initializedFields() { return x != null && y != null && r != null; }
    private boolean validR(float r) { return r >= 1 && r <= 3; }
    private boolean validCoordinate(float c) { return c >= -5 && c <= 5; }

    public boolean inArea() { return inRectangle() || inTriangle() || inCircle(); }
    private boolean inRectangle() { return x <= 0 && x >= -r && y >= 0 && y <= r/2; }
    private boolean inTriangle() { return x >= 0 && y <= 0 && y >= -r/2 + x/2; }
    private boolean inCircle() { return x <= 0 && y <= 0 && x*x + y*y <= r*r; }

    @Override
    public Point clone() {
        try {
            Point clone = (Point) super.clone();
            clone.x = x; clone.y = y; clone.r = r;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
