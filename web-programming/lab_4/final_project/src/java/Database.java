import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Singleton
public class Database {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @PostConstruct
    public void postConstruct() {
        entityManagerFactory = Persistence.createEntityManagerFactory("oracle");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @PreDestroy
    public void preDestroy() {
        entityManager.close();
        entityManagerFactory.close();
    }

    public boolean registration(String login, String password) throws Exception {
        User user = entityManager.find(User.class, login);
        if (user != null) return false;
        user = new User(login, encrypt(password));
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        return true;
    }

    public boolean authorization(String login, String password) throws Exception {
        User user = entityManager.find(User.class, login);
        if (user == null) return false;
        return user.getPassword().equals(encrypt(password));
    }

    private String encrypt(String str) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = MessageDigest.getInstance("SHA-1").digest(str.getBytes());
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b >> 4) & 15));
            sb.append(Integer.toHexString(b & 15));
        }
        return sb.toString().toUpperCase();
    }

    public void addPoint(String login, Point point) {
        User user = entityManager.find(User.class, login);
        user.getPoints().add(point);
        entityManager.getTransaction().begin();
        entityManager.persist(point);
        entityManager.merge(user);
        entityManager.getTransaction().commit();
    }

    public List<Point> getPoints(String login) {
        return entityManager.find(User.class, login).getPoints();
    }

    public void clearPoints(String login) {
        User user = entityManager.find(User.class, login);
        user.getPoints().clear();
        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();
    }
}
