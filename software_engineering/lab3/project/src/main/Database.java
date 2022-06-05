import javax.sql.DataSource;
import java.sql.*;
import java.util.function.Consumer;

/**
 * A class that provides an interface for working with the "points" table in the database.
 * @see Point
 */
public class Database {
    private final Connection connection;
    private final PreparedStatement INSERT_POINT;
    private final PreparedStatement SELECT_POINTS;
    private final PreparedStatement CLEAR_POINTS;

    /**
     * Establishes a connection to the database.
     * @param dataSource for getting the Connection instance.
     * @exception Exception failed to connect to the database.
     */
    public Database(DataSource dataSource) throws Exception {
        try {
            connection = dataSource.getConnection();
            INSERT_POINT = connection.prepareStatement("insert into points (x, y, r) values (?, ?, ?)");
            SELECT_POINTS = connection.prepareStatement("select x, y, r from points");
            CLEAR_POINTS = connection.prepareStatement("delete from points");
        } catch (SQLException e) {
            throw new Exception("The connection to the database could not be established.");
        }
    }

    /**
     * Loads all points from the database.
     * @param points the object to whom the points will be transferred.
     * @exception Exception failed to load points.
     */
    public void loadPoints(Consumer<Point> points) throws Exception {
        try {
            ResultSet resultSet = SELECT_POINTS.executeQuery();
            while (resultSet.next()) {
                Point newPoint = new Point(
                        resultSet.getFloat("x"),
                        resultSet.getFloat("y"),
                        resultSet.getFloat("r")
                );
                if (newPoint.valid()) points.accept(newPoint);
            }
            resultSet.close();
        } catch (SQLException e) { throw new Exception(
            "Error loading points from databases. Some points may not have been loaded."
        );}
    }

    /**
     * Enters a point into the database.
     * @param point the point to add to the database.
     * @exception Exception failed to add point.
     */
    public void addPoint(Point point) throws Exception {
        if (!point.valid()) throw new Exception("The point is incorrect.");
        try {
            INSERT_POINT.setFloat(1, point.getX());
            INSERT_POINT.setFloat(2, point.getY());
            INSERT_POINT.setFloat(3, point.getR());
            INSERT_POINT.executeUpdate();
        } catch (SQLException e) { throw new Exception(
            "The point could not be added due to an error when executing a query to the database."
        );}
    }

    /**
     * Deletes all points in the database.
     * @exception Exception failed to delete points.
     */
    public void clearPoints() throws Exception {
        try { CLEAR_POINTS.executeUpdate(); }
        catch (SQLException e) { throw new Exception(
            "Error when executing a query to the database about clearing the table."
        );}
    }

    /**
     * Terminates the connection to the database.
     */
    public void destroy() {
        close(SELECT_POINTS);
        close(INSERT_POINT);
        close(CLEAR_POINTS);
        close(connection);
    }

    private void close(AutoCloseable closeable) {
        try { closeable.close(); }
        catch (Exception ignored) { }
    }
}
