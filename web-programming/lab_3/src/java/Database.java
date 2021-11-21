import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.function.Consumer;

public class Database implements IDatabase {
    private Connection connection;

    public Database(String path) throws Exception {

    }

    @Override
    public void addPoint(Point point) {

    }

    @Override
    public void loadPoints(Consumer<Point> points) {

    }

    @Override
    public void clearPoints() {

    }
}