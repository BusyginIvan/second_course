import java.sql.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private final Connection connection;
    private final PreparedStatement INSERT_POINT;
    private final PreparedStatement SELECT_POINTS;
    private final PreparedStatement CLEAR_POINTS;

    public Database() throws Exception {
        String error = "Ошибка при подключении к БД! ";
        String prefix = "data_";

        String url = System.getenv(prefix + "URL");
        String login = System.getenv(prefix + "login");
        String password = System.getenv(prefix + "password");

        if (url == null) throw new Exception(error + "В переменных среды не задан URL.");
        if (login == null) throw new Exception(error + "В переменных среды не задан логин.");
        if (password == null) throw new Exception(error + "В переменных среды не задан пароль.");

        connection = DriverManager.getConnection(url, login, password);

        INSERT_POINT = connection.prepareStatement("insert into points (x, y, r) values (?, ?, ?)");
        SELECT_POINTS = connection.prepareStatement("select x, y, r from points");
        CLEAR_POINTS = connection.prepareStatement("delete from points");

        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
    }

    public void addPoint(Point point) {
        if (!point.valid()) return;
        try {
            INSERT_POINT.setFloat(1, point.getX());
            INSERT_POINT.setFloat(2, point.getY());
            INSERT_POINT.setFloat(3, point.getR());
            INSERT_POINT.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void loadPoints(Consumer<Point> points) {
        try {
            ResultSet resultSet = SELECT_POINTS.executeQuery();
            while (resultSet.next())
                points.accept(new Point(
                        resultSet.getFloat("x"),
                        resultSet.getFloat("y"),
                        resultSet.getFloat("r")
                ));
            resultSet.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void clearPoints() {
        try {
            CLEAR_POINTS.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private void destroy() {
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