package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Users;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.text.MessageFormat;

@Repository
public class UserRepository extends AbstractRepository<Users>{

    private static final String FIND_USER_BY_EMAIL_QUERY = "SELECT u FROM User u WHERE u.email = ''{0}''";
    private static final String FIND_USER_BY_USERNAME_QUERY = "SELECT u FROM User u WHERE u.username = ''{0}''";
    public Users findByEmail(String email) {
        try {
            Query query = entityManager.createQuery(MessageFormat.format(FIND_USER_BY_EMAIL_QUERY, email));
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Users findByUsername(String username) {
        try {
            Query query = entityManager.createQuery(MessageFormat.format(FIND_USER_BY_USERNAME_QUERY, username));
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


}
