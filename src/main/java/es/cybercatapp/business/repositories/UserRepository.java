package es.cybercatapp.business.repositories;

import es.cybercatapp.business.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.text.MessageFormat;

@Repository
public class UserRepository extends AbstractRepository<User>{

    private static final String FIND_USER_BY_EMAIL_QUERY = "SELECT u FROM User u WHERE u.email = ''{0}''";
    private static final String FIND_USER_BY_USERNAME_QUERY = "SELECT u FROM User u WHERE u.username = ''{0}''";
    public User findByEmail(String email) {
        try {
            Query query = entityManager.createQuery(MessageFormat.format(FIND_USER_BY_EMAIL_QUERY, email));
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public User findByUsername(String username) {
        try {
            Query query = entityManager.createQuery(MessageFormat.format(FIND_USER_BY_USERNAME_QUERY, username));
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


}
