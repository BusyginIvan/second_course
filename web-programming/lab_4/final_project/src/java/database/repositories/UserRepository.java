package database.repositories;

import database.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@Stateless
@Transactional
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void save(User user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(entityManager.find(User.class, username));
    }

    public boolean exists(String username) {
        return findByUsername(username).isPresent();
    }
}
