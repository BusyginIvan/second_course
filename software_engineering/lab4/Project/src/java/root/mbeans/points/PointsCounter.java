package root.mbeans.points;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class PointsCounter extends NotificationBroadcasterSupport implements PointsCounterMBean {
    private long hitPointsAmount = 0, pointsAmount = 0, missesCounter = 0;

    @Override public void deletePoints() { this.hitPointsAmount = 0; this.pointsAmount = 0; }

    @Override public void incPoints() { this.pointsAmount++; }

    @Override public void incHitPoints() { this.hitPointsAmount++; }

    @Override public long getPointsAmount() { return pointsAmount; }

    @Override public long getPointsHitAmount() { return hitPointsAmount; }

    @Override public void resetMisses() { missesCounter = 0; }

    @Override
    public void countMisses() {
        if (++missesCounter >= 4) {
            sendNotification(new Notification(
                "Count of misses in a row",
                this, missesCounter,
                "User has already got 4 misses in a row."
            ));
        }
    }
}
