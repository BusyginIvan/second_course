package root.mbeans.points;

public interface PointsCounterMBean {
    long getPointsAmount();
    long getPointsHitAmount();
    void deletePoints();
    void incPoints();
    void incHitPoints();
    void countMisses();
    void resetMisses();
}
