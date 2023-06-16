package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Inscriptions;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
public class InscriptionsRepository extends AbstractRepository<Inscriptions>{

    private static final String FIND_INSCRIPTION = "SELECT t FROM Inscription t where t.courses.courseId = :courseId AND t.users.username = :username";

    private static final String FIND_COURSES_BY_USERID = "SELECT t.courses FROM Inscription t where t.users.username = :userName";

    private static final String FIND_COURSES_BY_COUNTUSERS = "SELECT t.courses FROM Inscription t GROUP BY t.courses ORDER BY COUNT(t.users) DESC";


    public Inscriptions findInscription (Long courseId, String username) {
        try {
            TypedQuery<Inscriptions> query = entityManager.createQuery(FIND_INSCRIPTION, Inscriptions.class);
            query.setParameter("courseId", courseId);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public List<Courses> findCoursesByUser (String userName) {
        try {
            TypedQuery<Courses> query = entityManager.createQuery(FIND_COURSES_BY_USERID, Courses.class);
            query.setParameter("userName", userName);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Courses> findCoursesByCountUsers () {
        try {
            TypedQuery<Courses> query = entityManager.createQuery(FIND_COURSES_BY_COUNTUSERS, Courses.class);
            query.setFirstResult(0);
            query.setMaxResults(3);
            return query.getResultList();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
