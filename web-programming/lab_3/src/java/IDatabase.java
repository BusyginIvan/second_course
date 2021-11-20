import java.util.function.Consumer;

public interface IDatabase {
    void addPoint(Point point);
    void loadPoints(Consumer<Point> points);
    void clearPoints();
}