import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "points")
@Data @NoArgsConstructor
public class Point {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private float x, y, r;
    private boolean result;

    public Point(float x, float y, float r) {
        setX(x); setY(y); setR(r); updateResult();
    }

    public void updateResult() { result = inRectangle() || inTriangle() || inCircle(); }
    private boolean inRectangle() { return x >= 0 && x <= r/2 && y >= 0 && y <= r; }
    private boolean inTriangle() { return x >= 0 && y <= 0 && y >= -r + x; }
    private boolean inCircle() { return x <= 0 && y <= 0 && x*x + y*y <= r*r/4; }

    public boolean valid() {
        return validR(r) && validCoordinate(x) && validCoordinate(y);
    }
    private boolean validR(float r) { return r >= 1 && r <= 3; }
    private boolean validCoordinate(float c) { return c >= -5 && c <= 5; }
}
