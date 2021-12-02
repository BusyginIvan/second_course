import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
public class Points implements Serializable {
    private static final List<Point> pointList = new ArrayList<>();
    private static final Object LOCKER = new Object();

    private Database database = null;
    private Point newPoint = new Point();
    private String message;

    @Resource(name="jdbc/datasource")
    public void setDatabase(DataSource dataSource) {
        try {
            database = new Database(dataSource);
            database.loadPoints(pointList::add);
        } catch (Exception e) {
            message = e.getMessage();
        }
    }

    @PreDestroy
    public void destroy() { database.destroy(); }

    public List<Point> getPointList() { return pointList; }
    public Point getNewPoint() { return newPoint; }
    public String getMessage() { return message; }

    private void action(AutoCloseable databaseAction, Runnable localAction) {
        if (database == null) return;
        synchronized (LOCKER) {
            try {
                databaseAction.close();
            } catch (Exception e) {
                message = e.getMessage();
                return;
            }
            localAction.run();
        }
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