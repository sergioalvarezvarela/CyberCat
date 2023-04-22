package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Users;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.text.MessageFormat;

@Repository
public class UserRepository extends AbstractRepository<Users>{

    private static final String FIND_USER_BY_EMAIL_QUERY = "SELECT u FROM User u WHERE u.email = :email";
    private static final String FIND_USER_BY_USERNAME_QUERY = "SELECT u FROM User u WHERE u.username = :username";
    public Users findByEmail(String email) {
        try {
            Query query = entityManager.createQuery(FIND_USER_BY_EMAIL_QUERY);
            query.setParameter("email",email);
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Users findByUsername(String username) {
        try {
            Query query = entityManager.createQuery(FIND_USER_BY_USERNAME_QUERY);
            query.setParameter("username",username);
            return (Users) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }



}
