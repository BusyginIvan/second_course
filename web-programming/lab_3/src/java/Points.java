import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Points implements Serializable {
    private final List<Point> pointList;
    private final Database database;
    private final Lock lock;
    private Point newPoint;

    public Points() throws Exception {
        lock = new ReentrantLock();
        pointList = new ArrayList<>();
        newPoint = new Point();
        database = DatabaseBuilder.newDatabase("data_");
        database.loadPoints(pointList::add);
    }

    public List<Point> getPointList() { return pointList; }
    public Point getNewPoint() { return newPoint; }

    public void addPoint() {
        database.addPoint(newPoint);
        lock.lock();
        if (newPoint.valid()) {
            pointList.add(newPoint);
            newPoint = new Point();
        }
        lock.unlock();
    }

    public void clear() {
        lock.lock();
        pointList.clear();
        database.clearPoints();
        lock.unlock();
    }
}