import javax.faces.bean.ManagedBean;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.*;
import java.util.function.Consumer;

@ManagedBean
public class Database {
    private final Connection connection;
    private final PreparedStatement INSERT_POINT;
    private final PreparedStatement SELECT_POINTS;
    private final PreparedStatement CLEAR_POINTS;

    public Database(DataSource dataSource) throws Exception {
        try {
            connection = dataSource.getConnection();
            INSERT_POINT = connection.prepareStatement("insert into points (x, y, r) values (?, ?, ?)");
            SELECT_POINTS = connection.prepareStatement("select x, y, r from points");
            CLEAR_POINTS = connection.prepareStatement("delete from points");
        } catch (SQLException e) {
            throw new Exception("Не удалось установить соединение с базой данных.");
        }
    }

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
            "Ошибка при загрузке точек из баз данных. Возможно, некоторые точки так и не были загружены."
        );}
    }

    public void addPoint(Point point) throws Exception {
        if (!point.valid()) throw new Exception("Точка задана некорректно.");
        try {
            INSERT_POINT.setFloat(1, point.getX());
            INSERT_POINT.setFloat(2, point.getY());
            INSERT_POINT.setFloat(3, point.getR());
            INSERT_POINT.executeUpdate();
        } catch (SQLException e) { throw new Exception(
            "Не удалось добавить точку из-за ошибки при выполнении запроса к базе данных."
        );}
    }

    public void clearPoints() throws Exception {
        try { CLEAR_POINTS.executeUpdate(); }
        catch (SQLException e) { throw new Exception(
            "Ошибка при выполнении запроса к базе данных об очистке таблицы."
        );}
    }

    @PreDestroy
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