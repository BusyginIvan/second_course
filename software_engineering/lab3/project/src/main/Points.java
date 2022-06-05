import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that stores the data that the user interacts with.
 * Gives the client side of the application the ability to receive and save points.
 * It also stores a server error message that the user can see.
 * @see Point
 * @see Database
 */
@ManagedBean
public class Points implements Serializable {
    private static final List<Point> pointList = new ArrayList<>();
    private static final Object LOCKER = new Object();

    private Database database = null;
    private Point newPoint = new Point();
    private String message;

    /**
     * Creates an instance of the Database class and loads points from the database.
     * @param dataSource for creating the Database instance.
     */
    @Resource(name="jdbc/datasource")
    public void setDatabase(DataSource dataSource) {
        try {
            database = new Database(dataSource);
            database.loadPoints(pointList::add);
        } catch (Exception e) {
            message = e.getMessage();
        }
    }

    /** Terminates the connection to the database. */
    @PreDestroy
    public void destroy() { database.destroy(); }

    /**
     * Returns the list of all saved points.
     * @return list of all points.
     */
    public List<Point> getPointList() { return pointList; }

    /**
     * Returns the current point.
     * @return the current point.
     */
    public Point getNewPoint() { return newPoint; }

    /**
     * Returns a message with the server error text.
     * @return message.
     */
    public String getMessage() { return message; }

    private void action(AutoCloseable databaseAction, Runnable localAction) {
        if (database == null) return;
        synchronized (LOCKER) {
            try { databaseAction.close(); }
            catch (Exception e) { message = e.getMessage(); return; }
            localAction.run();
        }
    }

    /** Saves current point. */
    public void addPoint() {
        action(
            () -> database.addPoint(newPoint),
            () -> {
                pointList.add(newPoint);
                newPoint = new Point();
            }
        );
    }

    /** Deletes all points. */
    public void clear() {
        action(database::clearPoints, pointList::clear);
    }
}
