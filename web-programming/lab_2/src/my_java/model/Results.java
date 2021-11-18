package my_java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Results implements Serializable {
    private final List<Point> pointList = Collections.synchronizedList(new ArrayList<>());
    public Results() { }

    public List<Point> getPointList() { return pointList; }

    public void addPoint(Point point) { pointList.add(point); }

    public boolean isEmpty() { return pointList.isEmpty(); }
}