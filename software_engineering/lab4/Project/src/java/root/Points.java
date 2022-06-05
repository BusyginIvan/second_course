package root;

import root.mbeans.MBeanManager;
import root.mbeans.area.*;
import root.mbeans.points.*;

import javax.annotation.*;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean
public class Points implements Serializable {
    private static final Logger logger = Logger.getLogger(Points.class.getName());
    private static final Object LOCKER = new Object();

    private AreaMBean areaMBean;
    private PointsCounterMBean pointsCounterMBean;

    private Database database = null;
    private Point newPoint = new Point();
    private static final List<Point> pointList = new ArrayList<>();
    private String message;

    @Resource(name="jdbc/datasource")
    public void setDatabase(DataSource dataSource) {
        try {
            database = new Database(dataSource);
            database.loadPoints(pointList::add);
        } catch (Exception e) {
            message = e.getMessage();
        }
    }

    public static String getProcessID() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    @PostConstruct
    public void postConstruct() {
        logger.log(Level.INFO, "Process ID: " + getProcessID());

        areaMBean = new Area(this);
        pointsCounterMBean = new PointsCounter();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            MBeanManager.registerMBean(areaMBean, session.getId());
            MBeanManager.registerMBean(pointsCounterMBean, session.getId());
        }
    }

    @PreDestroy
    public void destroy() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            MBeanManager.unregisterMBean(areaMBean, session.getId());
            MBeanManager.unregisterMBean(pointsCounterMBean, session.getId());
        }
        if (database != null) database.destroy();
    }

    public List<Point> getPointList() { return pointList; }
    public Point getNewPoint() { return newPoint; }
    public String getMessage() { return message; }

    public void addPoint() {
        if (database == null) return;
        synchronized (LOCKER) {
            if (!newPoint.isValid()) {
                message = "Точка не была добавлена, так как неверно заданы значения координат или радиус.";
                return;
            }

            try { database.addPoint(newPoint); }
            catch (Exception e) { message = e.getMessage(); return; }

            if (newPoint.inArea()) {
                pointsCounterMBean.resetMisses();
                pointsCounterMBean.incPoints();
                pointsCounterMBean.incHitPoints();
            } else {
                pointsCounterMBean.countMisses();
                pointsCounterMBean.incPoints();
            }

            pointList.add(newPoint);
            newPoint = newPoint.clone();
        }
    }

    public void clearPoints() {
        if (database == null) return;
        synchronized (LOCKER) {
            try { database.clearPoints(); }
            catch (Exception e) { message = e.getMessage(); return; }
            pointList.clear();
            pointsCounterMBean.resetMisses();
            pointsCounterMBean.deletePoints();
        }
    }
}
