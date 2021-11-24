import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Results implements Serializable {
    private final List<Point> points;
    private final Database database;
    private final Lock lock;
    private Point newPoint;

    public Results() throws Exception {
        lock = new ReentrantLock();
        points = new ArrayList<>();
        newPoint = new Point();
        database = new Database();
        database.loadPoints(points::add);
    }

    public List<Point> getPoints() { return points; }
    public Point getNewPoint() { return newPoint; }

    public void addPoint() {
        lock.lock();
        if (newPoint.valid()) {
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