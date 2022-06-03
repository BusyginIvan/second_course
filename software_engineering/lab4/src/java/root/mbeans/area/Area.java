package root.mbeans.area;

import root.Points;

public class Area implements AreaMBean {
    private final Points points;

    public Area(Points points) { this.points = points; }

    @Override
    public double calculateArea() {
        float r = points.getNewPoint().getR();
        return r * r / 2 + 3.1415f * r * r / 4 + r * r / 2 / 2;
    }
}
