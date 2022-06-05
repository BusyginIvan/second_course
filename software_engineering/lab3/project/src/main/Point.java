import java.io.Serializable;

/**
 * Class for storing the coordinates of a point and the radius of an area.
 * It can also determine if a point belongs to an area.
 */
public class Point implements Serializable {
    private Float x, y, r = 3f;

    /**
     * Constructor without parameters.
     */
    public Point() { }
    /**
     * Constructor with parameters.
     * @param x the point coordinate.
     * @param y the point coordinate.
     * @param r the area radius.
     */
    public Point(Float x, Float y, Float r) { this.x = x; this.y = y; this.r = r; }


    /**
     * Returns the x point coordinate.
     * @return current x value.
     */
    public Float getX() { return x; }
    /**
     * Returns the y point coordinate.
     * @return current y value.
     */
    public Float getY() { return y; }
    /**
     * Returns the area radius.
     * @return current area radius.
     */
    public Float getR() { return r; }

    /**
     * Sets a new value for x. The number is rounded to three digits.
     * @param x new value for x.
     */
    public void setX(Float x) { this.x = Math.round(x * 1000) / 1000.f; }
    /**
     * Sets a new value for y. The number is rounded to three digits.
     * @param y new value for y.
     */
    public void setY(Float y) { this.y = Math.round(y * 1000) / 1000.f; }
    /**
     * Sets a new value for the area radius. The number is rounded to three digits.
     * @param r new value for the area radius.
     */
    public void setR(Float r) { this.r = Math.round(r * 1000) / 1000.f; }

    /**
     * Returns the result of checking whether the point belongs to the area.
     * @return "yes" - belongs, "no" - not belongs, "undefined" - coordinates or the radius are incorrect.
     */
    public String getResult() {
        if (valid()) return result() ? "yes" : "no";
        return "undefined";
    }

    /**
     * Checks whether the coordinates and radius are set correctly.
     * @return true, if coordinates and the radius are correct.
     */
    public boolean valid() {
        return initializedFields() && validR(r) && validCoordinate(x) && validCoordinate(y);
    }

    private boolean initializedFields() { return x != null && y != null && r != null; }

    private boolean validR(float r) { return r >= 1 && r <= 3; }

    private boolean validCoordinate(float c) { return c >= -5 && c <= 5; }

    private boolean result() { return inRectangle() || inTriangle() || inCircle(); }

    private boolean inRectangle() { return x <= 0 && x >= -r && y >= 0 && y <= r/2; }

    private boolean inTriangle() { return x >= 0 && y <= 0 && y >= -r/2 + x/2; }

    private boolean inCircle() { return x <= 0 && y <= 0 && x*x + y*y <= r*r; }
}
