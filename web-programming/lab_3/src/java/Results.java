import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Results implements Serializable {
    private final List<Point> points;
    private IDatabase database;
    private final Lock lock;
    private Point newPoint;
    /*private String message = "";*/

    public Results() throws Exception {
        lock = new ReentrantLock();
        points = new ArrayList<>();
        newPoint = new Point();
        database = new Database("database");
        database.loadPoints(points::add);
    }

    public List<Point> getPoints() { return points; }
    public Point getNewPoint() { return newPoint; }
    /*public String getMessage() { return message; }*/

    public void addPoint() {
        lock.lock();
        if (newPoint.valid()) {
            newPoint.initResult();
            points.add(newPoint);
            database.addPoint(newPoint);
            newPoint = new Point();
        }
        lock.unlock();
    }

    public void clear() {
        lock.lock();
        points.clear();
        database.clearPoints();
        lock.unlock();
    }
}