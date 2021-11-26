import java.io.Serializable;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Connection connection;
    private final PreparedStatement INSERT_POINT;
    private final PreparedStatement SELECT_POINTS;
    private final PreparedStatement CLEAR_POINTS;

    public Database(String url, String login, String password) throws Exception {
        connection = DriverManager.getConnection(url, login, password);

        INSERT_POINT = connection.prepareStatement("insert into points (x, y, r) values (?, ?, ?)");
        SELECT_POINTS = connection.prepareStatement("select x, y, r from points");
        CLEAR_POINTS = connection.prepareStatement("delete from points");

        logger.log(Level.INFO, "Установлено соединение с БД.");

        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
    }

    public void addPoint(Point point) {
        if (!point.valid()) return;
        executorService.execute(() -> {
            try {
                INSERT_POINT.setFloat(1, point.getX());
                INSERT_POINT.setFloat(2, point.getY());
                INSERT_POINT.setFloat(3, point.getR());
                INSERT_POINT.executeUpdate();
            } catch (SQLException e) {
                logger.log(Level.WARNING, e.getMessage());
            }
        });
    }

    public void loadPoints(Consumer<Point> points) {
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
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void clearPoints() {
        executorService.execute(() -> {
            try {
                CLEAR_POINTS.executeUpdate();
            } catch (SQLException e) {
                logger.log(Level.WARNING, e.getMessage());
            }
        });
    }

    public void destroy() {
        logger.log(Level.INFO, "Закрываем соединение с БД.");
        executorService.shutdown();
        while (true)
            try { if(executorService.awaitTermination(10, TimeUnit.SECONDS)) break; }
            catch (InterruptedException e) { break; }
        /*logger.log(Level.INFO, "Все процессы завершены: " + (executorService.isTerminated() ? "да" : "нет"));*/
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