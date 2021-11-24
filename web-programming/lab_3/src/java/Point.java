import java.io.Serializable;

public class Point implements Serializable {
    private Float x, y, r = 3f;
    private String result;

    public Float getX() { return x; }
    public Float getY() { return y; }
    public Float getR() { return r; }

    public void setX(Float x) { this.x = Math.round(x * 1000) / 1000.f; }
    public void setY(Float y) { this.y = Math.round(y * 1000) / 1000.f; }
    public void setR(Float r) { this.r = Math.round(r * 1000) / 1000.f; }

    public String getResult() { return result; }
    public void initResult() {
        if (valid()) result = result() ? "да" : "нет";
    }

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