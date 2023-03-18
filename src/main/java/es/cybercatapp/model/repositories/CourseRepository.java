package es.cybercatapp.model.repositories;

import es.cybercatapp.model.entities.Courses;
import es.cybercatapp.model.entities.Users;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.text.MessageFormat;
import java.util.List;

@Repository
public class CourseRepository extends AbstractRepository<Courses>{



    private static final String FIND_COURSES_BY_OWNER_QUERY = "SELECT c FROM Course c WHERE c.user_owner.id = :userId";

    public List<Courses> findCoursesByUserOwner(Long userId) {
        TypedQuery<Courses> query = entityManager.createQuery(FIND_COURSES_BY_OWNER_QUERY, Courses.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
