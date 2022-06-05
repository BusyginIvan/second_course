import org.junit.Test;

import static org.junit.Assert.*;

public class AreaTest {
    @Test
    public void topLeftTrue() {
        Point point = new Point(-1f, 1.5f, 3f);
        assertEquals("yes", point.getResult());

        Point point2 = new Point(-2f, 1f, 3f);
        assertEquals("yes", point2.getResult());

        Point point3 = new Point(-2.1f, 0.5f, 3f);
        assertEquals("yes", point3.getResult());
    }

    @Test

    public void topLeftFalse() {
        Point point = new Point(-1f, 4f, 3f);
        assertEquals("no", point.getResult());

        Point point2 = new Point(-2f, 4.1f, 3f);
        assertEquals("no", point2.getResult());

        Point point3 = new Point(-2.1f, 4.5f, 3f);
        assertEquals("no", point3.getResult());
    }

    @Test
    public void downLeftFalse() {
        Point point = new Point(-3f, -3f, 3f);
        assertEquals("no", point.getResult());

        Point point2 = new Point(-1.5f, -3f, 3f);
        assertEquals("no", point2.getResult());

        Point point3 = new Point(-4f, -4f, 3f);
        assertEquals("no", point3.getResult());
    }

    @Test
    public void downLeftTrue() {
        Point point = new Point(-1f, -1f, 3f);
        assertEquals("yes", point.getResult());

        Point point2 = new Point(-1.5f, -1.5f, 3f);
        assertEquals("yes", point2.getResult());

        Point point3 = new Point(-2.1f, -0.5f, 3f);
        assertEquals("yes", point3.getResult());
    }

    @Test
    public void downRightTrue() {
        Point point = new Point(3f, 0f, 3f);
        assertEquals("yes", point.getResult());

        Point point2 = new Point(1f, -1f, 3f);
        assertEquals("yes", point2.getResult());

        Point point3 = new Point(2f, -0.5f, 3f);
        assertEquals("yes", point3.getResult());
    }

    @Test
    public void downRightFalse() {
        Point point = new Point(3f, -3f, 3f);
        assertEquals("no", point.getResult());

        Point point2 = new Point(5f, -5f, 3f);
        assertEquals("no", point2.getResult());

        Point point3 = new Point(1f, -4f, 3f);
        assertEquals("no", point3.getResult());
    }
}
