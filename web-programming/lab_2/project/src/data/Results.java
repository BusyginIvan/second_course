package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Results implements Serializable {
    private final ArrayList<Point> list = new ArrayList<>();
    public Results() { }

    public void addPoint(Point point) { list.add(point); }

    public boolean isEmpty() { return list.isEmpty(); }

    public String toHTML() {
        return "<table id='outputTable'>" +
                "<tr> <th>x</th> <th>y</th> <th>r</th> <th>Точка входит в ОДЗ</th> </tr>\n" +
                list.stream().map(Point::toHTML).collect(Collectors.joining("\n")) +
                "</table>";
    }
}