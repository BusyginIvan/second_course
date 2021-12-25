package database.repositories;

import database.entities.Point;
import database.entities.User;
import database.exceptions.UserNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
public class PointsRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void add(String username, Point point) throws UserNotFoundException {
        User user = entityManager.find(User.class, username);
        if (user == null) throw new UserNotFoundException();
        user.getPoints().add(point);
        entityManager.persist(point);
        entityManager.merge(user);
        entityManager.flush();
    }

    public List<Point> getAll(String username) {
        return entityManager.find(User.class, username).getPoints();
    }

    public void clear(String username) {
        User user = entityManager.find(User.class, username);
        user.getPoints().clear();
        entityManager.merge(user);
        entityManager.flush();
    }
}
