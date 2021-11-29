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

    @Resource(name="jdbc/datasource")
    public void setDatabase(DataSource dataSource) throws Exception {
        database = new Database(dataSource);
        database.loadPoints(pointList::add);
    }

    public Points() {
        lock = new ReentrantLock();
        pointList = new ArrayList<>();
        newPoint = new Point();
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
        database.clearPoints();
        lock.lock();
        pointList.clear();
        lock.unlock();
    }
}