import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ManagedBean
public class Points implements Serializable {
    private final List<Point> pointList;
    private Database database;
    private final Lock lock;
    private Point newPoint;
    private String message;

    @Resource(name="jdbc/datasource")
    public void setDatabase(DataSource dataSource) {
        try {
            database = new Database(dataSource);
            database.loadPoints(pointList::add);
        } catch (Exception e) { message = e.getMessage(); }
    }

    public Points() {
        lock = new ReentrantLock();
        pointList = new ArrayList<>();
        newPoint = new Point();
    }

    public List<Point> getPointList() { return pointList; }
    public Point getNewPoint() { return newPoint; }
    public String getMessage() { return message; }

    private void action(AutoCloseable databaseAction, Runnable localAction) {
        if (database == null) return;
        lock.lock();
        try { databaseAction.close(); }
        catch (Exception e) {
            message = e.getMessage();
            lock.unlock();
            return;
        }
        localAction.run();
        lock.unlock();
    }

    public void addPoint() {
        action(
            () -> database.addPoint(newPoint),
            () -> {
                pointList.add(newPoint);
                newPoint = new Point();
            }
        );
    }

    public void clear() {
        action(database::clearPoints, pointList::clear);
    }
}