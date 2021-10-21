package data;

import java.io.Serializable;

public class Point implements Serializable {
    private final double x, y, r;
    private final boolean coordsStatus;

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        coordsStatus = checkCoordinates(x, y, r);
    }

    private boolean checkCoordinates(double x, double y, double r) {
        return  (x >= 0) && (x <= r/2) && (y >= -r) && (y <= 0) ||
                (x <= 0) && (y >= 0) && (y <= (r + x)/2) ||
                (x*x + y*y <= r*r/4) && (x <= 0) && (y <= 0);
    }

    public String toHTML() {
        return "<tr>" +
                "<td>" + format(x) + "</td>" +
                "<td>" + format(y) + "</td>" +
                "<td>" + format(r) + "</td>" +
                "<td style='color: " + ((coordsStatus) ? "green" : "red") + "'>" + coordsStatus + "</td>" +
                "</tr>";
    }

    private String format(double a) { return String.format("%.3f", a); }
}