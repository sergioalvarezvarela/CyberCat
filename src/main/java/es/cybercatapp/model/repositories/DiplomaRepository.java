package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Content;
import es.cybercatapp.model.entities.Diploma;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DiplomaRepository extends AbstractRepository<Diploma>{
    private static final String FIND_DIPLOMA_BY_COURSE_AND_USERNAME= "SELECT d FROM Diploma d WHERE d.users.username = :username AND d.courses.courseId = :courseId";

    private static final String FIND_DIPLOMA_BY_USERNAME= "SELECT d FROM Diploma d WHERE d.users.username = :username AND d.paymentCompleted = true";

    public Diploma findDiplomaByCourseAndUsername(Long courseId, String username) {
        try {
            TypedQuery<Diploma> query = entityManager.createQuery(FIND_DIPLOMA_BY_COURSE_AND_USERNAME, Diploma.class);
            query.setParameter("username", username);
            query.setParameter("courseId", courseId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }


    }

    public List<Diploma> inicializarDiploma(String username) {
        try {
            TypedQuery<Diploma> query = entityManager.createQuery(FIND_DIPLOMA_BY_USERNAME, Diploma.class);
            query.setParameter("username", username);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }


    }


}
